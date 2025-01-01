package com.hai.ollama.config;

import com.hai.ollama.service.DailyReportService;
import com.hai.ollama.service.MockWeatherService;
import com.hai.ollama.service.SummaryReportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;


import java.util.function.Function;

@Configuration
 class FunctionCallbackConfig {

    @Bean
    @Description("获取实时天气")
    public Function<MockWeatherService.Request, String> currentWeather() { // bean name as function name
        return new MockWeatherService();
    }

    @Bean
    @Description("记录日报")
    public Function<DailyReportService.Request, String> recordDailyReport() { // bean name as function name
        return new DailyReportService();
    }

    @Bean
    @Description("总结周报")
    public Function<SummaryReportService.Request, String> summaryDailyReport() { // bean name as function name
        return new SummaryReportService();
    }
}


