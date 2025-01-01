package com.hai.ollama.service;


import ch.qos.logback.classic.pattern.LineOfCallerConverter;

import java.util.List;
import java.util.function.Function;

public class MockWeatherService implements Function<MockWeatherService.Request, String> {

    public enum Unit { C, F }
    public record Request(List<String> location, Unit unit) {}
//    public record Response(double temp, Unit unit) {}
    public record Response(double temp, Unit unit) {}

    public String apply(Request request) {
        System.out.println("MockWeatherService：" + request.toString());
        List<String> location = request.location();
        location.forEach(l -> System.out.println("location:" + l));
//        float temp = 0;
//        if (location.equals("北京")) temp = 3.0f;
//        if (location.equals("上海")) temp = 12.5f;
//        if (location.equals("三亚")) temp = 23.6f;
        return "天气信息如下：北京为 3.0C /n上海为 12.5C /n三亚为 23.6C";
    }
    /*public Response apply(Request request) {
        System.out.println("MockWeatherService：" + request.toString());
        String[] location = request.location();
        float temp = 0;
        if (location.equals("北京")) temp = 3.0f;
        if (location.equals("上海")) temp = 12.5f;
        if (location.equals("三亚")) temp = 23.6f;
        return new Response(temp, Unit.C);
    }*/
}
