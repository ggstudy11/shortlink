package com.example.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_link_locale_stats")
public class ShortLinkLocaleStatsDO extends BaseDO {
    private Long id;

    private String fullShortUrl;

    private Date date;

    private Integer cnt;

    private String province;

    private String city;

    private String adcode;
}
