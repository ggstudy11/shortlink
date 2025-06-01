package com.example.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shortlink.admin.dao.entity.UserDO;
import com.example.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.example.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.example.shortlink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {
    /**
     * 通过用户名获取用户信息
     * @param username - 用户名
     * @return UserRespDTO
     */
    UserRespDTO getUserByUsername(String username);

    Boolean hasUsername(String username);

    void register(UserRegisterReqDTO userRegisterReqDTO);

    void update(UserUpdateReqDTO userUpdateReqDTO);
}
