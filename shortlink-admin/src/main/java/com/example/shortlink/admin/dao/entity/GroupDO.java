package com.example.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.shortlink.admin.common.database.BaseDO;

@TableName("t_group")
public class GroupDO extends BaseDO {

    private Long id;

    private String gid;

    private String name;

    private String username;

    private String sortOrder;
}
