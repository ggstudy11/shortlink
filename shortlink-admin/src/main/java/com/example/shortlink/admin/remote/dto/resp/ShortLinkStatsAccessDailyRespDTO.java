package com.example.shortlink.admin.remote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortLinkStatsAccessDailyRespDTO {

    private String date;

    private Integer pv;

    private Integer uv;

    private Integer uip;
}
