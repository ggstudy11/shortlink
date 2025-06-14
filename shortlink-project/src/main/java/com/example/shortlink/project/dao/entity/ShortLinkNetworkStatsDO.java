package com.example.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_link_network_stats")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkNetworkStatsDO extends BaseDO {

    private Long id;

    private String fullShortUrl;

    private Date date;

    private Integer cnt;

    private String network;
}
