package com.example.shortlink.project.dao.entity;

import com.example.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ShortLinkBrowserStatsDO extends BaseDO {

    private Long id;

    private String fullShortUrl;

    private Date date;

    private Integer cnt;

    private String browser;
}
