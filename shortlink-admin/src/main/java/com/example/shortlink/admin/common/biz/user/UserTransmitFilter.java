package com.example.shortlink.admin.common.biz.user;

import com.alibaba.fastjson2.JSON;
import com.example.shortlink.admin.common.constant.RedisCacheConstant;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * 用户信息传输过滤器
 */
@RequiredArgsConstructor
@Component
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final AuthProperties authProperties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        /* 检查是否为放行路径 */
        if (isExclude(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

        String token = httpServletRequest.getHeader("token");
        String username = httpServletRequest.getHeader("username");

        /* 头为空 返回401*/
        if (token == null || token.isEmpty() || username == null || username.isEmpty()) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        /* token过期或错误 返回401 */
        try {
            Object userInfoStr = stringRedisTemplate.opsForHash().get(RedisCacheConstant.LOGIN_USER + username, token);
            UserInfoDTO userINfoDTO = JSON.parseObject(userInfoStr.toString(), UserInfoDTO.class);
            UserContext.setUser(userINfoDTO);
        } catch (Exception e) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }

    private boolean isExclude(String antPath) {
        for (String pathPattern : authProperties.getExcludeUrls()) {
            if (antPathMatcher.match(pathPattern, antPath)) {
                return true;
            }
        }
        return false;
    }
}