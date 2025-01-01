package com.hai.ollama.service;


import com.hai.ollama.constant.OllamaConstant;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Service
//@Slf4j
public class SummaryReportService implements Function<SummaryReportService.Request, String> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public record Request(String dailyReport, String localDate) {}

    public String apply(Request request) {
        StringBuilder builder = new StringBuilder();
        try {
            System.out.println("SummaryReportService：" + request.toString());
            Set<String> keys = redisTemplate.keys(OllamaConstant.DR_KEY);
            if (keys != null) {
                keys.forEach(key -> {
                    String dailyReport = (String) redisTemplate.opsForValue().get(key);
                    builder.append(dailyReport).append("\n");
                });
            }
        } catch (Exception e) {
//            log.error("周报总结失败：{}", e.getMessage());
            return "抱歉，周报总结失败，请重试";
        }
//        log.info("周报总结成功");
        return builder.toString();
    }
    public static LocalDate getDate(LocalDate localDate) {
        LocalDate today = LocalDate.now();

        if (localDate.isEqual(today)) {
            return today;
        } else if (localDate.isEqual(today.minusDays(1))) {
            return today.minusDays(1);
        } else if (localDate.isEqual(today.plusDays(1))) {
            return today.plusDays(1);
        } else {
            throw new IllegalArgumentException("传入的日期不是今天、昨天或明天");
        }
    }
    public static List<LocalDate> getWeekDates() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<LocalDate> weekDates = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            weekDates.add(startOfWeek.plusDays(i));
        }

        return weekDates;
    }
}
