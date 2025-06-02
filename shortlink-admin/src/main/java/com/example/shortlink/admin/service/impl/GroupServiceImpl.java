package com.example.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.admin.common.biz.user.UserContext;
import com.example.shortlink.admin.dao.entity.GroupDO;
import com.example.shortlink.admin.dao.mapper.GroupMapper;
import com.example.shortlink.admin.dto.req.GroupAddReqDTO;
import com.example.shortlink.admin.dto.resp.GroupListRespDTO;
import com.example.shortlink.admin.service.GroupService;
import com.example.shortlink.admin.util.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void save(GroupAddReqDTO groupAddReqDTO) {
        String gid;
        do {
            gid = RandomGenerator.generate();
        } while (hasGid(gid));

        GroupDO groupDO = GroupDO.builder()
                .username(UserContext.getUsername())
                .gid(gid)
                .sortOrder(0)
                .name(groupAddReqDTO.getName())
                .build();

        baseMapper.insert(groupDO);
    }

    private boolean hasGid(String gid) {

        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername());

        return baseMapper.exists(wrapper);
    }

    @Override
    public List<GroupListRespDTO> listGroup() {

        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .orderByDesc(List.of(GroupDO::getSortOrder, GroupDO::getUpdateTime))
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername());

        List<GroupDO> groupDOList = baseMapper.selectList(wrapper);

        return BeanUtil.copyToList(groupDOList, GroupListRespDTO.class);
    }
}
