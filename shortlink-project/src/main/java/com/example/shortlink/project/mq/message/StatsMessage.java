package com.example.shortlink.project.mq.message;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatsMessage {

    private Integer uv;

    private Integer newIp;

    private Date now;

    private String fullShortUrl;

    private String os;

    private String browser;

    private String device;

    private String network;

    private String user;

    private String adcode;

    private String province;

    private String city;

    private String ip;
}
