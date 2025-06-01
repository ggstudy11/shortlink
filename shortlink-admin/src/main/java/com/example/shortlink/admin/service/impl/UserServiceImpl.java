package com.example.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.admin.common.constant.RedisCacheConstant;
import com.example.shortlink.admin.common.convention.exception.ClientException;
import com.example.shortlink.admin.common.enums.UserErrorCode;
import com.example.shortlink.admin.dao.entity.UserDO;
import com.example.shortlink.admin.dao.mapper.UserMapper;
import com.example.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.example.shortlink.admin.dto.resp.UserRespDTO;
import com.example.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(wrapper);

        if (userDO == null) {
            throw new ClientException(UserErrorCode.USER_NOT_EXIST);
        }

        UserRespDTO userRespDTO = new UserRespDTO();
        BeanUtils.copyProperties(userDO, userRespDTO);
        return userRespDTO;
    }

    @Override
    public Boolean hasUsername(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO userRegisterReqDTO) {
        if (hasUsername(userRegisterReqDTO.getUsername())) {
            throw new ClientException(UserErrorCode.USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER + userRegisterReqDTO.getUsername());
        if (lock.tryLock()) {
            try {
                UserDO userDO = new UserDO();
                BeanUtils.copyProperties(userRegisterReqDTO, userDO);

                int insert = baseMapper.insert(userDO);
                if (insert != 1) {
                    throw new ClientException(UserErrorCode.USER_CREATE_ERROR);
                }

                userRegisterCachePenetrationBloomFilter.add(userDO.getUsername());
            } finally {
                lock.unlock();
            }
        } else {
            throw new ClientException(UserErrorCode.USER_EXIST);
        }

    }
}
