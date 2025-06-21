package com.example.shortlink.gateway.filter;

import java.io.ObjectInputFilter.Config;
import java.net.URLEncoder;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;


import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GlobalAuthFilter extends AbstractGatewayFilterFactory<Config> {

    private final AuthProperties authProperties;
    private final StringRedisTemplate stringRedisTemplate;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            
            if (isExclude(request.getPath().toString())) {
                return chain.filter(exchange);
            }

            String token = request.getHeaders().getFirst("token");
            String username = request.getHeaders().getFirst("username");
            if (token == null || token.isEmpty() || username == null || username.isEmpty()) {
                ServerHttpResponse response = exchange.getResponse();
                response.setRawStatusCode(401);
                return response.setComplete();
            }

            try {
                Object userInfoStr = stringRedisTemplate.opsForHash().get("short-link:login_user" + username, token);
                JSONObject userInfoJson = JSON.parseObject(userInfoStr.toString());
                ServerHttpRequest.Builder builder = exchange.getRequest().mutate().headers(httpHeaders -> {
                    httpHeaders.set("username", userInfoJson.getString("username"));
                });
                return chain.filter(exchange.mutate().request(builder.build()).build());
            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setRawStatusCode(401);
                return response.setComplete();
            }
        };
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
