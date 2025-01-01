package com.hai.ollama.controller;

import com.hai.ollama.service.MockWeatherService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.springframework.ai.ollama.api.OllamaOptions.create;

@RestController
@RequestMapping(value ="ollama")
public class FunctionCallbackController {

    private final OllamaChatModel ollamaChatModel;

    public FunctionCallbackController(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }

    @RequestMapping("/callback/weather")
    public String callback(@RequestParam(value = "message", defaultValue = "北京、上海和三亚的天气怎么样？") String message) {
        String system = """
      您是“天天好心情”天气预报公司的客户支持代理。请以友好、乐于助人且愉快的方式来回复。
      您正在通过在线聊天系统与客户互动。
      您能够支持我国城市天气信息查询，如果用户问的问题不支持请告知详情。
         如果需要，您可以调用 currentWeather 函数辅助完成。
         请讲中文。
         请回答以下问题：
      """;
        ChatResponse call = ollamaChatModel.call(new Prompt(system + message, create()
                .withFunctions(Set.of("currentWeather")).build())); // Enable the function
        return call.getResult().getOutput().getContent();
    }

    @RequestMapping("/callback/recordDailyReport")
    public String recordDailyReportCallback(@RequestParam(value = "message", defaultValue = "帮我记录日报：今天完成了Spring-AI-Chat的开发工作、帮助新入职的同事解决开发问题。") String message) {
        String system = """
      您是一个善于记录日报的好助手。请以友好、乐于助人且愉快的方式来回复。
      您正在通过在线聊天系统与客户互动。
      您能够支持用户日报的记录，如果用户问的问题不支持请告知详情。
         如果需要，您可以调用 recordDailyReport 函数辅助完成。
         请讲中文。
      """;
        ChatResponse call = ollamaChatModel.call(new Prompt(system + message, create()
                .withFunction("recordDailyReport").build())); // Enable the function
        return call.getResult().getOutput().getContent();
    }

    @RequestMapping("/callback/summaryDailyReport")
    public String summaryDailyReportCallback(@RequestParam(value = "message", defaultValue = "帮我总结周报") String message) {
        String system = """
      您是一个善于根据日报信息总结周报的好助手。请以友好、乐于助人且愉快的方式来回复。
      您正在通过在线聊天系统与客户互动。
      您能够支持用户周报的总结，如果用户问的问题不支持请告知详情。
         如果需要，您可以调用 summaryWeeklyReport 函数辅助完成。
         请讲中文。
      """;
        ChatResponse call = ollamaChatModel.call(new Prompt(system + message, create()
                .withFunction("summaryWeeklyReport").build())); // Enable the function
        return call.getResult().getOutput().getContent();
    }
}
