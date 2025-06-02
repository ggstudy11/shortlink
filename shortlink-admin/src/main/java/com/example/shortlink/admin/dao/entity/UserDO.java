package com.example.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.shortlink.admin.common.database.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户持久层
 */
@TableName("t_user")
@Data
@EqualsAndHashCode(callSuper=true)
public class UserDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 删除时间戳
     */
    private Long deletionTime;

}