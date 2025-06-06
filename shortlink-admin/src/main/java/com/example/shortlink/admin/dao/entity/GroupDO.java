package com.example.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.shortlink.admin.common.database.BaseDO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("t_group")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class GroupDO extends BaseDO {

    private Long id;

    private String gid;

    private String name;

    private String username;

    private Integer sortOrder;
}
