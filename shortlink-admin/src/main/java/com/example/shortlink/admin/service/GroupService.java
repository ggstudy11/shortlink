package com.example.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shortlink.admin.dao.entity.GroupDO;
import com.example.shortlink.admin.dto.req.GroupAddReqDTO;

public interface GroupService extends IService<GroupDO> {

    void save(GroupAddReqDTO groupAddReqDTO);
}
