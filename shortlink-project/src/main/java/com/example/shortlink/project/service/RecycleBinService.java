package com.example.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shortlink.project.dao.entity.ShortLinkDO;
import com.example.shortlink.project.dto.req.ShortLinkToBinReqDTO;

public interface RecycleBinService extends IService<ShortLinkDO> {
    void removeToBin(ShortLinkToBinReqDTO shortLinkToBinReqDTO);
}
