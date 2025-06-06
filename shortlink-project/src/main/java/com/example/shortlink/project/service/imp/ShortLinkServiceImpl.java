package com.example.shortlink.project.service.imp;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.project.common.convention.exception.ServiceException;
import com.example.shortlink.project.dao.entity.ShortLinkDO;
import com.example.shortlink.project.dao.mapper.ShortLinkMapper;
import com.example.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkCountRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.example.shortlink.project.service.ShortLinkService;
import com.example.shortlink.project.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    @Override
    public ShortLinkCreateRespDTO create(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        String shortLinkSuffix = generateSuffix(shortLinkCreateReqDTO.getDomain(), shortLinkCreateReqDTO.getOriginUrl());
        ShortLinkDO shortLinkDO = BeanUtil.toBean(shortLinkCreateReqDTO, ShortLinkDO.class);
        shortLinkDO.setFullShortUrl(shortLinkCreateReqDTO.getDomain() + "/" + shortLinkSuffix);
        shortLinkDO.setShortUri(shortLinkSuffix);
        shortLinkDO.setEnableStatus(1);

        /* 这里应该是有问题的 */

//        try {
//            baseMapper.insert(shortLinkDO);
//        } catch (DuplicateKeyException e) {
//            throw new ServiceException("短链接生成重复");
//        }
        baseMapper.insert(shortLinkDO);
        shortUriCreateCachePenetrationBloomFilter.add(shortLinkDO.getFullShortUrl());
        return ShortLinkCreateRespDTO.builder()
                .gid(shortLinkCreateReqDTO.getGid())
                .fullShortLink(shortLinkDO.getFullShortUrl())
                .originUrl(shortLinkCreateReqDTO.getOriginUrl())
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
}
