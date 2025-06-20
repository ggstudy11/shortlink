package com.example.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.admin.common.biz.user.UserContext;
import com.example.shortlink.admin.common.convention.exception.ClientException;
import com.example.shortlink.admin.dao.entity.GroupDO;
import com.example.shortlink.admin.dao.mapper.GroupMapper;
import com.example.shortlink.admin.dto.req.GroupAddReqDTO;
import com.example.shortlink.admin.dto.req.GroupSortReqDTO;
import com.example.shortlink.admin.dto.req.GroupUpdateReqDTO;
import com.example.shortlink.admin.dto.resp.GroupListRespDTO;
import com.example.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.example.shortlink.admin.remote.dto.resp.ShortLinkCountRespDTO;
import com.example.shortlink.admin.service.GroupService;
import com.example.shortlink.admin.util.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    private final ShortLinkRemoteService shortLinkRemoteService;

    @Override
    public void save(GroupAddReqDTO groupAddReqDTO) {
       save(groupAddReqDTO.getName(), UserContext.getUsername());
    }

    @Override
    public void save(String name, String username) {

        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, username)
                .eq(GroupDO::getDelFlag, 0);

        List<GroupDO> groupDOList = baseMapper.selectList(wrapper);

        if (groupDOList != null && groupDOList.size() == 10) {
            throw new ClientException("已创建分组数达到上限");
        }

        String gid;
        do {
            gid = RandomGenerator.generate();
        } while (hasGid(gid, username));

        GroupDO groupDO = GroupDO.builder()
                .username(username)
                .gid(gid)
                .sortOrder(0)
                .name(name)
                .build();

        baseMapper.insert(groupDO);
    }

    private boolean hasGid(String gid, String username) {

        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, username);

        return baseMapper.exists(wrapper);
    }

    @Override
    public List<GroupListRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .orderByDesc(List.of(GroupDO::getSortOrder, GroupDO::getUpdateTime))
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername());

        List<GroupDO> groupDOList = baseMapper.selectList(wrapper);
        List<ShortLinkCountRespDTO> shortLinkCountRespDTOList = shortLinkRemoteService.countShortLink(groupDOList.stream().map(GroupDO::getGid).toList()).getData();
        List<GroupListRespDTO> results = BeanUtil.copyToList(groupDOList, GroupListRespDTO.class);
        Map<String, Integer> counts = shortLinkCountRespDTOList.stream().collect(Collectors.toMap(ShortLinkCountRespDTO::getGid, ShortLinkCountRespDTO::getShortLinkCount));
        return results.stream().peek(result -> result.setShortLinkCount(counts.get(result.getGid()))).toList();
    }

    @Override
    public void updateGroup(GroupUpdateReqDTO groupUpdateReqDTO) {

        LambdaUpdateWrapper<GroupDO> wrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getGid, groupUpdateReqDTO.getGid())
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);

        GroupDO groupDO = GroupDO.builder()
                        .name(groupUpdateReqDTO.getName())
                        .build();

        baseMapper.update(groupDO, wrapper);
    }

    @Override
    public void deleteGroup(String gid) {

        LambdaUpdateWrapper<GroupDO> wrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);

        GroupDO groupDO = GroupDO.builder().build();
        groupDO.setDelFlag(1);

        baseMapper.update(groupDO, wrapper);
    }

    @Override
    public void sortGroup(List<GroupSortReqDTO> groupSortReqDTOList) {
        groupSortReqDTOList.forEach(groupSortReqDTO -> {
            GroupDO groupDO = GroupDO.builder()
                    .sortOrder(groupSortReqDTO.getSortOrder())
                    .build();

            LambdaUpdateWrapper<GroupDO> wrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getGid, groupSortReqDTO.getGid())
                    .eq(GroupDO::getUsername, UserContext.getUsername())
                    .eq(GroupDO::getDelFlag, 0);

            baseMapper.update(groupDO, wrapper);
        });
    }
}
