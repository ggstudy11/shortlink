package com.example.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.shortlink.project.common.database.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@TableName("t_link")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShortLinkDO extends BaseDO {

    private Long id;

    private String domain;

    private String shortUri;

    private String fullShortUrl;

    private String originUrl;

    private Integer clickNum;

    private String gid;

    private Integer enableStatus;

    private Integer createdType;

    private Integer validDateType;

    private Date validDate;

    @TableField("`describe`")
    private String describe;
}
