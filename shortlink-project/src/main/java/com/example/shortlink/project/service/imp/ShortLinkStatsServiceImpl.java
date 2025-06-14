package com.example.shortlink.project.service.imp;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.example.shortlink.project.dao.mapper.ShortLinkAccessStatsMapper;
import com.example.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.example.shortlink.project.dto.resp.ShortLinkBasicStatsRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsAccessDailyRespDTO;
import com.example.shortlink.project.dto.resp.ShortLinkStatsRespDTO;
import com.example.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShortLinkStatsServiceImpl implements ShortLinkStatsService {

    private final ShortLinkAccessStatsMapper shortLinkAccessStatsMapper;

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


        return ShortLinkStatsRespDTO.builder()
                .pv(shortLinkBasicStatsRespDTO.getPv())
                .uv(shortLinkBasicStatsRespDTO.getUv())
                .uip(shortLinkBasicStatsRespDTO.getUip())
                .daily(daily)
                .build();
    }
}
