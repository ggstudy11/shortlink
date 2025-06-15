package com.example.shortlink.project.dto.resp;

import lombok.Data;

@Data
public class ShortLinkStatsUvRespDTO {

    private String uvType;

    private Integer cnt;

    private Double ratio;
}
