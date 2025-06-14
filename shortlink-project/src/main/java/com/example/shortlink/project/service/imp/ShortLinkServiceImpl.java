package com.example.shortlink.project.service.imp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.project.common.constant.RedisCacheConstant;
import com.example.shortlink.project.common.convention.exception.ServiceException;
import com.example.shortlink.project.dao.entity.*;
import com.example.shortlink.project.dao.mapper.*;
import com.example.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkCountRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.example.shortlink.project.service.ShortLinkService;
import com.example.shortlink.project.utils.HashUtil;
import com.example.shortlink.project.utils.LinkUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final ShortLinkAccessStatsMapper shortLinkAccessStatsMapper;
    private final ShortLinkLocaleStatsMapper shortLinkLocaleStatsMapper;
    private final ShortLinkOsStatsMapper shortLinkOsStatsMapper;
    private final ShortLinkBrowserStatsMapper shortLinkBrowserStatsMapper;
    private final ShortLinkAccessLogsMapper shortLinkAccessLogsMapper;
    private final ShortLinkDeviceStatsMapper shortLinkDeviceStatsMapper;
    private final ShortLinkNetworkStatsMapper shortLinkNetworkStatsMapper;


    @Override
    public ShortLinkCreateRespDTO create(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        String shortLinkSuffix = generateSuffix(shortLinkCreateReqDTO.getDomain(), shortLinkCreateReqDTO.getOriginUrl());
        ShortLinkDO shortLinkDO = BeanUtil.toBean(shortLinkCreateReqDTO, ShortLinkDO.class);
        shortLinkDO.setFullShortUrl(shortLinkCreateReqDTO.getDomain() + "/" + shortLinkSuffix);
        shortLinkDO.setShortUri(shortLinkSuffix);
        shortLinkDO.setEnableStatus(1);
        shortLinkDO.setFavicon(getFavicon(shortLinkCreateReqDTO.getOriginUrl()));
        /* 这里应该是有问题的 */

//        try {
//            baseMapper.insert(shortLinkDO);
//        } catch (DuplicateKeyException e) {
//            throw new ServiceException("短链接生成重复");
//        }

        baseMapper.insert(shortLinkDO);
        stringRedisTemplate.opsForValue().set(RedisCacheConstant.SHORT_URL_PREFIX + shortLinkDO.getFullShortUrl(), shortLinkDO.getOriginUrl(), LinkUtil.getShortLinkCacheTime(shortLinkDO.getValidDate()), TimeUnit.MILLISECONDS);
        shortUriCreateCachePenetrationBloomFilter.add(shortLinkDO.getFullShortUrl());
        return ShortLinkCreateRespDTO.builder()
                .gid(shortLinkCreateReqDTO.getGid())
                .fullShortLink(shortLinkDO.getFullShortUrl())
                .originUrl(shortLinkCreateReqDTO.getOriginUrl())
                .favicon(shortLinkDO.getFavicon())
                .build();
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO shortLinkPageReqDTO) {

        LambdaQueryWrapper<ShortLinkDO> wrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, shortLinkPageReqDTO.getGid())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0);

        IPage<ShortLinkDO> pageResult = baseMapper.selectPage(shortLinkPageReqDTO, wrapper);
        return pageResult.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespDTO.class));
    }

    @Override
    public List<ShortLinkCountRespDTO> countShortLink(List<String> gids) {
        Map<String, Integer> gidCountMap = gids.stream()
                .collect(Collectors.toMap(
                        gid -> gid,
                        val -> 0
                ));

        QueryWrapper<ShortLinkDO> wrapper = Wrappers.query(new ShortLinkDO())
                .select("gid, count(*) as shortLinkCount")
                .in("gid", gids)
                .eq("enable_status", 1)
                .eq("del_flag", 0)
                .groupBy("gid");
        List<Map<String, Object>> mapList = baseMapper.selectMaps(wrapper);

        mapList.forEach(map -> {
            String gid = map.get("gid").toString();
            Integer count = Integer.valueOf(map.get("shortLinkCount").toString());
            gidCountMap.put(gid, count);
        });

        return gidCountMap.entrySet().stream()
                .map(entry -> {
                    ShortLinkCountRespDTO dto = new ShortLinkCountRespDTO();
                    dto.setGid(entry.getKey());
                    dto.setShortLinkCount(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateShortLink(ShortLinkUpdateReqDTO shortLinkUpdateReqDTO) {

        LambdaQueryWrapper<ShortLinkDO> wrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, shortLinkUpdateReqDTO.getOriginGid())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getFullShortUrl, shortLinkUpdateReqDTO.getFullShortUrl());
        ShortLinkDO shortLinkDO = baseMapper.selectOne(wrapper);

        if (shortLinkDO != null) {
            if (shortLinkUpdateReqDTO.getNewGid() != null) {
                baseMapper.deleteById(shortLinkDO.getId());
                ShortLinkDO newShortLink = new ShortLinkDO();
                BeanUtils.copyProperties(shortLinkDO, newShortLink);
                newShortLink.setId(null);
                newShortLink.setGid(shortLinkUpdateReqDTO.getNewGid());
                BeanUtils.copyProperties(shortLinkUpdateReqDTO, newShortLink);
                baseMapper.insert(newShortLink);
            }
            else {
                BeanUtils.copyProperties(shortLinkUpdateReqDTO, shortLinkDO);
                baseMapper.update(shortLinkDO, wrapper);
            }
        } else {
            throw new RuntimeException("短链接记录不存在");
        }
    }

    @Override
    public void restoreShortUri(String shortLink, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String domain = request.getServerName();

        String fullShortUrl = domain + '/' + shortLink;

        // 缓存穿透
        if (!shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl)) {
            response.sendRedirect("/page/notfound");
            return;
        }

        // 不包含肯定不存在，但是存在也有误判情况，查一次缓存判断
        String originUrl = stringRedisTemplate.opsForValue().get(RedisCacheConstant.SHORT_URL_PREFIX + fullShortUrl);

        if (StringUtil.isNotBlank(originUrl)) {
            // 空标识
            if ("-".equals(originUrl)) {
                response.sendRedirect("/page/notfound");
                return;
            }
            recordStats(fullShortUrl, request, response);
            response.sendRedirect(originUrl);
            return;
        }

        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_SHORT_URL_PREFIX + fullShortUrl);
        lock.lock();
        try {
            originUrl = stringRedisTemplate.opsForValue().get(RedisCacheConstant.SHORT_URL_PREFIX + fullShortUrl);
            if (StringUtil.isNotBlank(originUrl)) {
                recordStats(fullShortUrl, request, response);
                response.sendRedirect(originUrl);
                return;
            }

            LambdaQueryWrapper<ShortLinkDO> wrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, fullShortUrl)
                    .eq(ShortLinkDO::getEnableStatus, 1)
                    .eq(ShortLinkDO::getDelFlag, 0);

            ShortLinkDO shortLinkDO = baseMapper.selectOne(wrapper);
            if (shortLinkDO == null || (shortLinkDO.getValidDate() != null && shortLinkDO.getValidDate().before(new Date()))) {
                // 缓存空标识
                stringRedisTemplate.opsForValue().set(RedisCacheConstant.SHORT_URL_PREFIX + fullShortUrl, "-", 30, TimeUnit.SECONDS);
                response.sendRedirect("/page/notfound");
            } else {
                stringRedisTemplate.opsForValue().set(RedisCacheConstant.SHORT_URL_PREFIX + fullShortUrl, shortLinkDO.getOriginUrl(), LinkUtil.getShortLinkCacheTime(shortLinkDO.getValidDate()), TimeUnit.MILLISECONDS);
                recordStats(fullShortUrl, request, response);
                response.sendRedirect(shortLinkDO.getOriginUrl());
            }
        } finally {
            lock.unlock();
        }
    }

    private void recordStats(String fullShortUrl, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        boolean isNewVisitor = true;
        String uvCookie = UUID.fastUUID().toString();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("uv".equals(cookie.getName())) {
                    isNewVisitor = false;
                    uvCookie = cookie.getValue();
                    break;
                }
            }
        }

        if (isNewVisitor) {
            Cookie cookie = new Cookie("uv", uvCookie);
            cookie.setPath(fullShortUrl.substring(fullShortUrl.indexOf("/")));
            // 一个月
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);
        }

        String ip = LinkUtil.getIp(request);
        int newIp = stringRedisTemplate.opsForSet().add(RedisCacheConstant.FULL_SHORT_URL_UIP_PREFIX + fullShortUrl, ip).intValue();

        Date now = new Date();
        int hour = DateUtil.hour(now, true);
        int weekday = DateUtil.dayOfWeekEnum(now).getIso8601Value();
        ShortLinkAccessStatsDO shortLinkAccessStatsDO = ShortLinkAccessStatsDO.builder()
                .uv(isNewVisitor ? 1 : 0)
                .pv(1)
                .uip(newIp)
                .date(now)
                .hour(hour)
                .weekday(weekday)
                .fullShortUrl(fullShortUrl)
                .build();
        shortLinkAccessStatsMapper.insertOrUpdate(shortLinkAccessStatsDO);

        ShortLinkLocaleStatsDO localeStats = LinkUtil.getLocaleStats(ip, fullShortUrl);
        shortLinkLocaleStatsMapper.insertOrUpdate(localeStats);

        String userAgent = request.getHeader("User-Agent");

        String os = LinkUtil.getOperatingSystem(userAgent);
        ShortLinkOsStatsDO shortLinkOsStatsDO = ShortLinkOsStatsDO.builder()
                .os(os)
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(now)
                .build();
        shortLinkOsStatsMapper.insertOrUpdate(shortLinkOsStatsDO);

        String browser = LinkUtil.getBrowser(userAgent);
        ShortLinkBrowserStatsDO shortLinkBrowserStatsDO = ShortLinkBrowserStatsDO.builder()
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(now)
                .browser(browser)
                .build();
        shortLinkBrowserStatsMapper.insertOrUpdate(shortLinkBrowserStatsDO);

        ShortLinkAccessLogsDO shortLinkAccessLogsDO = ShortLinkAccessLogsDO.builder()
                .fullShortUrl(fullShortUrl)
                .ip(ip)
                .os(os)
                .browser(browser)
                .user(uvCookie)
            .build();
        shortLinkAccessLogsMapper.insert(shortLinkAccessLogsDO);

        String device = LinkUtil.getDevice(userAgent);
        ShortLinkDeviceStatsDO shortLinkDeviceStatsDO = ShortLinkDeviceStatsDO.builder()
                .device(device)
                .fullShortUrl(fullShortUrl)
                .cnt(1)
                .date(now)
                .build();
        shortLinkDeviceStatsMapper.insertOrUpdate(shortLinkDeviceStatsDO);

        String network = LinkUtil.getNetwork(request);
        ShortLinkNetworkStatsDO shortLinkNetworkStatsDO = ShortLinkNetworkStatsDO.builder()
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .network(network)
                .date(now)
                .build();
        shortLinkNetworkStatsMapper.insertOrUpdate(shortLinkNetworkStatsDO);
    }

    private String generateSuffix(String domain, String originUrl) {
        int generateCount = 0;
        String shortUri = "";
        while (true) {
            if (generateCount++ > 10) {
                throw new ServiceException("短链接生成频繁，请稍后再试");
            }
            shortUri = HashUtil.hashToBase62(originUrl + System.currentTimeMillis());
            if (!shortUriCreateCachePenetrationBloomFilter.contains(domain + '/' + shortUri)) {
                break;
            }
        }
        return shortUri;
    }

    /**
     * 获取网站的favicon图标链接
     * @param url 网站的URL
     * @return favicon图标链接，如果不存在则返回null
     */
    private String getFavicon(String url) {
        try {
            URL targetUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                String redirectUrl = connection.getHeaderField("Location");
                if (redirectUrl != null) {
                    URL newUrl = new URL(redirectUrl);
                    connection = (HttpURLConnection) newUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    responseCode = connection.getResponseCode();
                }
            }


            if (responseCode == HttpURLConnection.HTTP_OK) {
                Document document = Jsoup.connect(url).get();
                Element faviconLink = document.select("link[rel~=(^|\\s)(shortcut|icon)(\\s|$)]").first();
                if (faviconLink != null) {
                    return faviconLink.attr("abs:href");
                }
            }
        } catch (Exception e) {
            log.warn("提取网站图标失败");
        }
        return null;
    }
}
