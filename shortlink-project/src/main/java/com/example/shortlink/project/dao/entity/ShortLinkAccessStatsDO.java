package com.example.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_link_access_stats")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkAccessStatsDO extends BaseDO {

    private Long id;

    private String fullShortUrl;

    private Date date;

    private Integer pv;

    private Integer uv;

    private Integer uip;

    private Integer hour;

    private Integer weekday;
}
