package com.example.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.shortlink.project.common.database.BaseDO;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_link_access_logs")
public class ShortLinkAccessLogsDO extends BaseDO {

    private Long id;

    private String fullShortUrl;

    private String user;

    private String browser;

    private String os;

    private String ip;
}
