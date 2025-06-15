package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shortlink.project.dao.entity.ShortLinkDO;
import com.example.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShortLinkMapper extends BaseMapper<ShortLinkDO> {

    void increStats(@Param("fullShortUrl") String fullShortUrl, @Param("uv") Integer uv, @Param("pv") Integer pv, @Param("uip") Integer uip);

    IPage<ShortLinkPageRespDTO> pageShortLink(Page<ShortLinkPageRespDTO> page, @Param("dto") ShortLinkPageReqDTO shortLinkPageReqDTO);
}
