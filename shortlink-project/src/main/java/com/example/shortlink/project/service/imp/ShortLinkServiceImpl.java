package com.example.shortlink.project.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.project.dao.entity.ShortLinkDO;
import com.example.shortlink.project.dao.mapper.ShortLinkMapper;
import com.example.shortlink.project.service.ShortLinkService;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
}
