package com.example.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shortlink.admin.dao.entity.GroupDO;
import com.example.shortlink.admin.dto.req.GroupAddReqDTO;
import com.example.shortlink.admin.dto.req.GroupUpdateReqDTO;
import com.example.shortlink.admin.dto.resp.GroupListRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {

    void save(GroupAddReqDTO groupAddReqDTO);

    List<GroupListRespDTO> listGroup();

    void updateGroup(GroupUpdateReqDTO groupUpdateReqDTO);
}
