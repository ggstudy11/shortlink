package com.example.shortlink.project.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkStatsRespDTO {

    private Integer pv;

    private Integer uv;

    private Integer uip;

    /* 基础访问详情 */
    private List<ShortLinkStatsAccessDailyRespDTO> daily;

    /* 地区访问详情 */
    private List<ShortLinkStatsLocaleCNRespDTO> localeCnStats;

    /* 每小时访问量 */
    private List<Integer> hourStats;

    /* 高频访问ip */
    private List<ShortLinkStatsTopIpRespDTO> topIpStats;

    /* 星期访问详情 */
    private List<Integer> weekdayStats;

    /* 浏览器访问详情 */
    private List<ShortLinkStatsBrowserRespDTO> browserStats;

    /* 操作系统访问详情 */
    private List<ShortLinkStatsOsRespDTO> osStats;

    /* 访客类型访问详情 */
    private List<ShortLinkStatsUvRespDTO> uvTypeStats;

    /* 设备类型访问详情 */
    private List<ShortLinkStatsDeviceRespDTO> deviceStats;

    /* 网络类型访问详情 */
    private List<ShortLinkStatsNetworkRespDTO> networkStats;
}
