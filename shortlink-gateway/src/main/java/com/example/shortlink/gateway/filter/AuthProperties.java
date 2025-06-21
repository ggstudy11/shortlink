package com.example.shortlink.gateway.filter;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "auth")
@Data
public class AuthProperties {
    private List<String> excludeUrls;
}
