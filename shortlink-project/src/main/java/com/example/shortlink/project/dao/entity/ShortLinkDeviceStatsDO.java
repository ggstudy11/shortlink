package com.example.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_link_device_stats")
@EqualsAndHashCode(callSuper = true)
public class ShortLinkDeviceStatsDO extends BaseDO {

    private Long id;

    private String fullShortUrl;

    private Date date;

    private Integer cnt;

    private String device;
}
