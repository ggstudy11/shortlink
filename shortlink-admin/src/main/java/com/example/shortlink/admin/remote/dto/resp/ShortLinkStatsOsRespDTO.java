package com.example.shortlink.admin.remote.dto.resp;

import lombok.Data;

@Data
public class ShortLinkStatsOsRespDTO {

    private Integer cnt;

    private String os;

    private Double ratio;
}
