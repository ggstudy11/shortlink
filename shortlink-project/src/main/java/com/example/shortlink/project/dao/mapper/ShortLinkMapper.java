package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShortLinkMapper extends BaseMapper<ShortLinkDO> {

    void increStats(@Param("fullShortUrl") String fullShortUrl, @Param("uv") Integer uv, @Param("pv") Integer pv, @Param("uip") Integer uip);
}
