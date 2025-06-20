package com.example.shortlink.project.mq.consumer;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.example.shortlink.project.common.convention.exception.AbstractException;
import com.example.shortlink.project.dao.entity.*;
import com.example.shortlink.project.dao.mapper.*;
import com.example.shortlink.project.mq.message.StatsMessage;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsConsumer {

    private final ShortLinkAccessLogsMapper shortLinkAccessLogsMapper;
    private final ShortLinkAccessStatsMapper shortLinkAccessStatsMapper;
    private final ShortLinkBrowserStatsMapper shortLinkBrowserStatsMapper;
    private final ShortLinkDeviceStatsMapper shortLinkDeviceStatsMapper;
    private final ShortLinkLocaleStatsMapper shortLinkLocaleStatsMapper;
    private final ShortLinkNetworkStatsMapper shortLinkNetworkStatsMapper;
    private final ShortLinkOsStatsMapper shortLinkOsStatsMapper;
    private final ShortLinkMapper shortLinkMapper;

    @RabbitListener(queues = "stats.queue")
    public void processMessage(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String payload = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info("收到信息:{}", payload);

            StatsMessage statsMessage = JSON.parseObject(payload, StatsMessage.class);
            saveAccessStats(statsMessage);
            saveAccessLogs(statsMessage);
            saveBrowserStats(statsMessage);
            saveDeviceStats(statsMessage);
            saveLocaleStats(statsMessage);
            saveNetworkStats(statsMessage);
            saveOsStats(statsMessage);
            increStats(statsMessage);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("消息处理失败", e);
            boolean requeue = shouldRequeue(e);
            channel.basicReject(deliveryTag, requeue);
        }
    }

    private boolean shouldRequeue(Exception e) {
        return !(e instanceof AbstractException);
    }

    private void saveAccessStats(StatsMessage statsMessage) {
        Integer uv = statsMessage.getUv();
        Integer newIp = statsMessage.getNewIp();
        Date now = statsMessage.getNow();
        int hour = DateUtil.hour(now, true);
        int weekday = DateUtil.dayOfWeekEnum(now).getIso8601Value();
        String fullShortUrl = statsMessage.getFullShortUrl();
        ShortLinkAccessStatsDO shortLinkAccessStatsDO = ShortLinkAccessStatsDO.builder()
                .uv(uv)
                .pv(1)
                .uip(newIp)
                .date(now)
                .hour(hour)
                .weekday(weekday)
                .fullShortUrl(fullShortUrl)
                .build();
        shortLinkAccessStatsMapper.insert(shortLinkAccessStatsDO);
    }

    private void saveOsStats(StatsMessage statsMessage) {
        String os = statsMessage.getOs();
        String fullShortUrl = statsMessage.getFullShortUrl();
        Date now = statsMessage.getNow();
        ShortLinkOsStatsDO shortLinkOsStatsDO = ShortLinkOsStatsDO.builder()
                .os(os)
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(now)
                .build();
        shortLinkOsStatsMapper.insertOrUpdate(shortLinkOsStatsDO);
    }

    private void saveBrowserStats(StatsMessage statsMessage) {
        String browser = statsMessage.getBrowser();
        String fullShortUrl = statsMessage.getFullShortUrl();
        Date now = statsMessage.getNow();
        ShortLinkBrowserStatsDO shortLinkBrowserStatsDO = ShortLinkBrowserStatsDO.builder()
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(now)
                .browser(browser)
                .build();
        shortLinkBrowserStatsMapper.insertOrUpdate(shortLinkBrowserStatsDO);
    }

    private void saveDeviceStats(StatsMessage statsMessage) {
        String device = statsMessage.getDevice();
        String fullShortUrl = statsMessage.getFullShortUrl();
        Date now = statsMessage.getNow();
        ShortLinkDeviceStatsDO shortLinkDeviceStatsDO = ShortLinkDeviceStatsDO.builder()
                .device(device)
                .fullShortUrl(fullShortUrl)
                .cnt(1)
                .date(now)
                .build();
        shortLinkDeviceStatsMapper.insertOrUpdate(shortLinkDeviceStatsDO);
    }

    private void saveNetworkStats(StatsMessage statsMessage) {
        String network = statsMessage.getNetwork();
        String fullShortUrl = statsMessage.getFullShortUrl();
        Date now = statsMessage.getNow();
        ShortLinkNetworkStatsDO shortLinkNetworkStatsDO = ShortLinkNetworkStatsDO.builder()
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .network(network)
                .date(now)
                .build();
        shortLinkNetworkStatsMapper.insertOrUpdate(shortLinkNetworkStatsDO);
    }

    private void saveLocaleStats(StatsMessage statsMessage) {
        String adcode = statsMessage.getAdcode();
        String fullShortUrl = statsMessage.getFullShortUrl();
        Date now = statsMessage.getNow();
        String province = statsMessage.getProvince();
        String city = statsMessage.getCity();
        ShortLinkLocaleStatsDO shortLinkLocaleStatsDO = ShortLinkLocaleStatsDO.builder()
                .adcode(adcode)
                .fullShortUrl(fullShortUrl)
                .province(province)
                .city(city)
                .date(now)
                .cnt(1)
                .build();
        shortLinkLocaleStatsMapper.insertOrUpdate(shortLinkLocaleStatsDO);
    }

    private void saveAccessLogs(StatsMessage statsMessage) {
        String ip = statsMessage.getIp();
        String os = statsMessage.getOs();
        String browser = statsMessage.getBrowser();
        String user = statsMessage.getUser();
        String device = statsMessage.getDevice();
        String network = statsMessage.getNetwork();
        String locale = statsMessage.getProvince() + "-" + statsMessage.getCity();
        locale = statsMessage.getProvince().equals("Unknown") ? "Unknown-" + locale : "中国-" + locale;
        String fullShortUrl = statsMessage.getFullShortUrl();
        ShortLinkAccessLogsDO shortLinkAccessLogsDO = ShortLinkAccessLogsDO.builder()
                .fullShortUrl(fullShortUrl)
                .ip(ip)
                .os(os)
                .browser(browser)
                .user(user)
                .device(device)
                .network(network)
                .locale(locale)
                .build();
        shortLinkAccessLogsMapper.insert(shortLinkAccessLogsDO);
    }

    private void increStats(StatsMessage statsMessage) {
        shortLinkMapper.increStats(statsMessage.getFullShortUrl(), statsMessage.getUv(), 1, statsMessage.getNewIp());
    }
}
