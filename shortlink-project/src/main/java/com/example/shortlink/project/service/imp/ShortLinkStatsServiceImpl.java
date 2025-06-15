package com.example.shortlink.project.service.imp;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.example.shortlink.project.dao.mapper.ShortLinkAccessLogsMapper;
import com.example.shortlink.project.dao.mapper.ShortLinkAccessStatsMapper;
import com.example.shortlink.project.dao.mapper.ShortLinkBrowserStatsMapper;
import com.example.shortlink.project.dao.mapper.ShortLinkLocaleStatsMapper;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.*;
import com.example.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShortLinkStatsServiceImpl implements ShortLinkStatsService {

    private final ShortLinkAccessStatsMapper shortLinkAccessStatsMapper;
    private final ShortLinkLocaleStatsMapper shortLinkLocaleStatsMapper;
    private final ShortLinkAccessLogsMapper shortLinkAccessLogsMapper;
    private final ShortLinkBrowserStatsMapper shortLinkBrowserStatsMapper;

    @Override
    public ShortLinkStatsRespDTO getLinkStats(ShortLinkStatsReqDTO shortLinkStatsReqDTO) {

        /* 获取指定时间段内uv pv uip总和 */
        ShortLinkBasicStatsRespDTO shortLinkBasicStatsRespDTO = shortLinkAccessStatsMapper.countBasicStats(shortLinkStatsReqDTO);

        /* 获取每天的uv pv uip */
        List<ShortLinkStatsAccessDailyRespDTO> resultList =
                shortLinkAccessStatsMapper.countStatsDaily(shortLinkStatsReqDTO);
        Map<String, ShortLinkStatsAccessDailyRespDTO> resultMap = resultList.stream()
                .collect(Collectors.toMap(ShortLinkStatsAccessDailyRespDTO::getDate, dto -> dto));
        List<String> allDates = DateUtil.rangeToList(DateUtil.parse(shortLinkStatsReqDTO.getStartDate()), DateUtil.parse(shortLinkStatsReqDTO.getEndDate()), DateField.DAY_OF_MONTH).stream()
                .map(DateUtil::formatDate)
                .toList();
        List<ShortLinkStatsAccessDailyRespDTO> daily = new ArrayList<>();
        for (String date : allDates) {
            ShortLinkStatsAccessDailyRespDTO shortLinkStatsAccessDailyRespDTO = resultMap.get(date);
            if (shortLinkStatsAccessDailyRespDTO == null) {
                shortLinkStatsAccessDailyRespDTO = ShortLinkStatsAccessDailyRespDTO.builder()
                        .date(date)
                        .pv(0)
                        .uip(0)
                        .uv(0)
                        .build();
            }
            daily.add(shortLinkStatsAccessDailyRespDTO);
        }

        /* 获取地区统计数据 */
        List<ShortLinkStatsLocaleCNRespDTO> localeCnStats = shortLinkLocaleStatsMapper.countStats(shortLinkStatsReqDTO);
        int sumOfLocale = localeCnStats.stream().mapToInt(ShortLinkStatsLocaleCNRespDTO::getCnt).sum();
        localeCnStats.forEach(each -> {
            double ratio = BigDecimal.valueOf(each.getCnt())
                    .divide(BigDecimal.valueOf(sumOfLocale), 4, RoundingMode.HALF_UP)  // 中间计算保留4位
                    .setScale(2, RoundingMode.HALF_UP)  // 最终结果保留2位
                    .doubleValue();

            each.setRatio(ratio);
        });

        /* 获取每小时访问数 */
        List<ShortLinkHourStatsRespDTO> hourStatsList = shortLinkAccessStatsMapper.countStatsHour(shortLinkStatsReqDTO);
        Map<Integer, Integer> hoursStatsMap = hourStatsList.stream().collect(Collectors.toMap(ShortLinkHourStatsRespDTO::getHour, ShortLinkHourStatsRespDTO::getPv));
        List<Integer> hourStats = new ArrayList<>();
        for (int i = 0; i < 24; ++i) {
            Integer pv = hoursStatsMap.get(i);
            hourStats.add(pv == null ? 0 : pv);
        }

        /* 高频ip访问数 */
        List<ShortLinkStatsTopIpRespDTO> topIpStats = shortLinkAccessLogsMapper.getTopIp(shortLinkStatsReqDTO);

        /* 星期访问数 */
        List<ShortLinkDayStatsRespDTO> daysList = shortLinkAccessStatsMapper.countStatsDay(shortLinkStatsReqDTO);
        Map<Integer, Integer> daysMap = daysList.stream().collect(Collectors.toMap(ShortLinkDayStatsRespDTO::getWeekday, ShortLinkDayStatsRespDTO::getPv));
        List<Integer> weekdayStats = new ArrayList<>();
        for (int i = 1; i <= 7; ++i) {
            Integer pv = daysMap.get(i);
            weekdayStats.add(pv == null ? 0 : pv);
        }

        /* 浏览器访问详情 */
        List<ShortLinkStatsBrowserRespDTO> browserStats = shortLinkBrowserStatsMapper.getBrowserStats(shortLinkStatsReqDTO);
        int sumOfBrowser = browserStats.stream().mapToInt(ShortLinkStatsBrowserRespDTO::getCnt).sum();
        browserStats.forEach(each -> {
            double ratio = BigDecimal.valueOf(each.getCnt())
                    .divide(BigDecimal.valueOf(sumOfBrowser), 4, RoundingMode.HALF_UP)  // 中间计算保留4位
                    .setScale(2, RoundingMode.HALF_UP)  // 最终结果保留2位
                    .doubleValue();

            each.setRatio(ratio);
        });


        return ShortLinkStatsRespDTO.builder()
                .pv(shortLinkBasicStatsRespDTO.getPv())
                .uv(shortLinkBasicStatsRespDTO.getUv())
                .uip(shortLinkBasicStatsRespDTO.getUip())
                .daily(daily)
                .localeCnStats(localeCnStats)
                .hourStats(hourStats)
                .topIpStats(topIpStats)
                .weekdayStats(weekdayStats)
                .browserStats(browserStats)
                .build();
    }
}
