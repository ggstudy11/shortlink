package com.example.shortlink.project.service.imp;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.project.common.convention.exception.ServiceException;
import com.example.shortlink.project.dao.entity.ShortLinkDO;
import com.example.shortlink.project.dao.mapper.ShortLinkMapper;
import com.example.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.example.shortlink.project.service.ShortLinkService;
import com.example.shortlink.project.utils.HashUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    public ShortLinkServiceImpl(RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter) {
        this.shortUriCreateCachePenetrationBloomFilter = shortUriCreateCachePenetrationBloomFilter;
    }

    @Override
    public ShortLinkCreateRespDTO create(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        String shortLinkSuffix = generateSuffix(shortLinkCreateReqDTO.getDomain(), shortLinkCreateReqDTO.getOriginUrl());
        ShortLinkDO shortLinkDO = BeanUtil.toBean(shortLinkCreateReqDTO, ShortLinkDO.class);
        shortLinkDO.setFullShortUrl(shortLinkCreateReqDTO.getDomain() + "/" + shortLinkSuffix);
        shortLinkDO.setShortUri(shortLinkSuffix);
        shortLinkDO.setEnableStatus(1);
        try {
            baseMapper.insert(shortLinkDO);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("短链接生成重复");
        }
        return ShortLinkCreateRespDTO.builder()
                .gid(shortLinkCreateReqDTO.getGid())
                .fullShortLink(shortLinkDO.getFullShortUrl())
                .originUrl(shortLinkCreateReqDTO.getOriginUrl())
                .build();
    }

    private String generateSuffix(String domain, String originUrl) {
        int generateCount = 0;
        String shortUri = "";
        while (true) {
            if (generateCount++ > 10) {
                throw new ServiceException("短链接生成频繁，请稍后再试");
            }
            shortUri = HashUtil.hashToBase62(originUrl);
            if (!shortUriCreateCachePenetrationBloomFilter.contains(domain + '/' + shortUri)) {
                break;
            }
        }
        return shortUri;
    }
}
