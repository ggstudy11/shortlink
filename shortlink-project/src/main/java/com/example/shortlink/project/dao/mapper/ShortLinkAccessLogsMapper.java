package com.example.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.project.dao.entity.ShortLinkAccessLogsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShortLinkAccessLogsMapper extends BaseMapper<ShortLinkAccessLogsDO> {
}
