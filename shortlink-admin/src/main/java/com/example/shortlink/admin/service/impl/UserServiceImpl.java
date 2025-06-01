package com.example.shortlink.admin.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.admin.common.constant.RedisCacheConstant;
import com.example.shortlink.admin.common.convention.exception.ClientException;
import com.example.shortlink.admin.common.enums.UserErrorCode;
import com.example.shortlink.admin.dao.entity.UserDO;
import com.example.shortlink.admin.dao.mapper.UserMapper;
import com.example.shortlink.admin.dto.req.UserLoginReqDTO;
import com.example.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.example.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.example.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.example.shortlink.admin.dto.resp.UserRespDTO;
import com.example.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

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

    @Override
    public void update(UserUpdateReqDTO userUpdateReqDTO) {
        // TODO 检查登陆用户是否与当前用户名一致，防止越权访问

        LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate(UserDO.class).eq(UserDO::getUsername, userUpdateReqDTO.getUsername());
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userUpdateReqDTO, userDO);
        int update = baseMapper.update(userDO, wrapper);
        if (update != 1) {
            throw new ClientException(UserErrorCode.USER_UPDATE_ERROR);
        }
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, userLoginReqDTO.getUsername())
                .eq(UserDO::getPassword, userLoginReqDTO.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(wrapper);
        if (userDO == null) {
            throw new ClientException(UserErrorCode.USER_LOGIN_ERROR);
        }

        if (stringRedisTemplate.hasKey(RedisCacheConstant.LOGIN_USER + userDO.getUsername())) {
            throw new ClientException(UserErrorCode.USER_ALREADY_LOGIN);
        }

        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(RedisCacheConstant.LOGIN_USER + userDO.getUsername(), token, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(RedisCacheConstant.LOGIN_USER + userDO.getUsername(), 2, TimeUnit.HOURS);
        return new UserLoginRespDTO(token);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get(RedisCacheConstant.LOGIN_USER + username, token) != null;
    }
}
