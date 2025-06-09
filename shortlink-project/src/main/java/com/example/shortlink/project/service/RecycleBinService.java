package com.example.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shortlink.project.dao.entity.ShortLinkDO;
import com.example.shortlink.project.dto.req.ShortLinkInBinPageReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkRmBinReqDTO;
import com.example.shortlink.project.dto.req.ShortLinkToBinReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkInBinPageRespDTO;

public interface RecycleBinService extends IService<ShortLinkDO> {
    void removeToBin(ShortLinkToBinReqDTO shortLinkToBinReqDTO);

    IPage<ShortLinkInBinPageRespDTO> pageInBin(ShortLinkInBinPageReqDTO shortLinkInBinPageReqDTO);

    void removeFromBin(ShortLinkRmBinReqDTO shortLinkRmBinReqDTO);
}
