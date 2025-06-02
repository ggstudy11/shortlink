package com.example.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.admin.dao.entity.GroupDO;
import com.example.shortlink.admin.dao.mapper.GroupMapper;
import com.example.shortlink.admin.dto.req.GroupAddReqDTO;
import com.example.shortlink.admin.service.GroupService;
import com.example.shortlink.admin.util.RandomGenerator;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void save(GroupAddReqDTO groupAddReqDTO) {
        String gid;
        do {
            gid = RandomGenerator.generate();
        } while (hasGid(gid));

        // TODO some attribute doesnt add
        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .name(groupAddReqDTO.getName())
                .build();

        baseMapper.insert(groupDO);
    }

    private boolean hasGid(String gid) {

        // TODO replace username
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, null);

        return baseMapper.exists(wrapper);
    }
}
