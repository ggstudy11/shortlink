package com.example.shortlink.project.dto.resp;

import lombok.Data;

@Data
public class ShortLinkStatsNetworkRespDTO {

    private Integer cnt;

    private String network;

    private Double ratio;
}
