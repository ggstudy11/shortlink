package com.example.shortlink.admin.remote.dto.resp;

import lombok.Data;

@Data
public class ShortLinkStatsUvRespDTO {

    private String uvType;

    private Integer cnt;

    private Double ratio;
}
