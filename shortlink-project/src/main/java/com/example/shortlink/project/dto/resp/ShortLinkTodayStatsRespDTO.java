package com.example.shortlink.project.dto.resp;

import lombok.Data;

@Data
public class ShortLinkTodayStatsRespDTO {

    private Integer todayPv;

    private Integer todayUv;

    private Integer todayUip;
}
