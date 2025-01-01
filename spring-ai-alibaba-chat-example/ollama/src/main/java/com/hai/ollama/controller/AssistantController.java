package com.hai.ollama.controller;

import com.hai.ollama.service.DailyReportAssistant;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Set;

import static org.springframework.ai.ollama.api.OllamaOptions.create;

@RestController
@RequestMapping(value ="ollama/assistant")
public class AssistantController {

    private final DailyReportAssistant agent;

    public AssistantController(DailyReportAssistant agent) {
        this.agent = agent;
    }

    @RequestMapping(path = "/report/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public Flux<String> chat(String chatId, String userMessage) {
            return agent.chat(chatId, userMessage);
        }
}
