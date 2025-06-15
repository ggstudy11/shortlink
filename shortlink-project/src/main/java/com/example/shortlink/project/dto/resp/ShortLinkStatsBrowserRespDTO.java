package com.example.shortlink.project.dto.resp;

import lombok.Data;

@Data
public class ShortLinkStatsBrowserRespDTO {

    private Integer cnt;

    private String browser;

    private Double ratio;
}
