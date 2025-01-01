package com.hai.ollama.service;


import com.hai.ollama.constant.OllamaConstant;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Service
//@Slf4j
public class DailyReportService implements Function<DailyReportService.Request, String> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public record Request(String dailyReport, String localDate) {}

    public String localDate = LocalDate.now().toString();


    public String apply(Request request) {
        try {
            System.out.println("DailyReportService：" + request.toString());
            redisTemplate.opsForValue().set(OllamaConstant.DR_KEY + localDate, request.dailyReport);
        } catch (Exception e) {
//            log.error("日志记录失败：{}", e.getMessage());
            return "抱歉，日志记录失败，请重试";
        }
//        log.info("日志记录成功");
        return "日志记录成功";
    }
}
