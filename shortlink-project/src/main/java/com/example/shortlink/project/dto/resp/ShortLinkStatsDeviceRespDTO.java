package com.example.shortlink.project.dto.resp;

import lombok.Data;

@Data
public class ShortLinkStatsDeviceRespDTO {

    private String device;

    private Integer cnt;

    private Double ratio;
}
