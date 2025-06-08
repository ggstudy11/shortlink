package com.example.shortlink.project.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.project.common.constant.RedisCacheConstant;
import com.example.shortlink.project.dao.entity.ShortLinkDO;
import com.example.shortlink.project.dao.mapper.ShortLinkMapper;
import com.example.shortlink.project.dto.req.ShortLinkToBinReqDTO;
import com.example.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements RecycleBinService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void removeToBin(ShortLinkToBinReqDTO shortLinkToBinReqDTO) {
        LambdaQueryWrapper<ShortLinkDO> wrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, shortLinkToBinReqDTO.getGid())
                .eq(ShortLinkDO::getFullShortUrl, shortLinkToBinReqDTO.getFullShortUrl())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0);

        ShortLinkDO shortLinkDO = new ShortLinkDO();
        shortLinkDO.setEnableStatus(0);
        baseMapper.update(shortLinkDO, wrapper);
        stringRedisTemplate.delete(RedisCacheConstant.SHORT_URL_PREFIX + shortLinkToBinReqDTO.getFullShortUrl());
    }
}
