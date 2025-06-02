package com.example.shortlink.admin.config;

import com.example.shortlink.admin.common.biz.user.UserTransmitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户配置自动装配
 */
@Configuration
@RequiredArgsConstructor
public class UserConfiguration {

    /**
     * 用户信息传递过滤器
     */
    @Bean
    public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter(UserTransmitFilter userTransmitFilter) {
        FilterRegistrationBean<UserTransmitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(userTransmitFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }
}
