package com.example.shortlink.admin.remote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkStatsLocaleCNRespDTO {

    private Integer cnt;

    private String locale;

    private Double ratio;
}
