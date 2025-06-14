package com.example.shortlink.project.dao.entity;

import com.example.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkLocaleStatsDO extends BaseDO {
    private Long id;

    private String fullShortUrl;

    private Date date;

    private Integer cnt;

    private String province;

    private String city;

    private String adcode;
}
