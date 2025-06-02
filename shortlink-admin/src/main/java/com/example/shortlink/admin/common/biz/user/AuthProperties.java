package com.example.shortlink.admin.common.biz.user;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "auth")
@Data
public class AuthProperties {
    private List<String> excludeUrls;
}
