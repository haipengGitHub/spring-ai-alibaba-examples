package com.hai.ollama.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

import static org.springframework.ai.ollama.api.OllamaOptions.create;

@RestController
@RequestMapping(value ="ollama")
public class OllamaChatController {

    public OllamaChatController(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }
    private final OllamaChatModel ollamaChatModel;

    /**
     *使用yml中ollama默认配置
     * @param message 问题
     * @return 回答
     */
    @GetMapping("/simple/chat")
    public String simpleChat(@RequestParam(value = "message") String message) {
       return ollamaChatModel.call(message);
    }

    /**
     * 自定义参数
     * @param message 问题
     * @return 回答
     */
    @GetMapping("/customize/chat")
    public String customizeChat(@RequestParam(value = "message") String message) {
        ChatResponse call = ollamaChatModel.call(new Prompt(message, create()
                .withModel("qwen2.5:1.5b")
                .withTopK(3)
                .withTopP(0.75)
                .withTemperature(0.4)
        ));
        System.out.println("call:" + call);
        return call.getResult().getOutput().getContent();
    }

    /**
     * 流式输出
     * @param message 问题信息
     * @return 回答信息
     */
    @PostMapping(value ="/stream/chat", produces = "text/event-stream")
    public Flux<ChatResponse> ollamaStream(@RequestParam(value = "message") String message) {
        Prompt prompt = new Prompt(message, create()
                .withModel("qwen2.5:1.5b")
                .withTemperature(0.4));
        return ollamaChatModel.stream(prompt);
    }

    /**
     * 自定义参数
     * @param message 问题
     * @return 回答
     */
    @GetMapping("/structured/chat")
    public String structuredChat(@RequestParam(value = "message", defaultValue = "我如何求解8x+7=-23") String message) {
        String jsonSchema = """
        {
            "type": "object",
            "properties": {
                "steps": {
                    "type": "array",
                    "items": {
                        "type": "object",
                        "properties": {
                            "explanation": { "type": "string" },
                            "output": { "type": "string" }
                        },
                        "required": ["explanation", "output"],
                        "additionalProperties": false
                    }
                },
                "final_answer": { "type": "string" }
            },
            "required": ["steps", "final_answer"],
            "additionalProperties": false
        }
        """;
        ChatResponse response = ollamaChatModel.call(new Prompt(message, create()
                            .withModel("qwen2.5:1.5b")
                            .withFormat(jsonSchema)
                            .build()
                ));
        return response.getResult().getOutput().getContent();
    }
}
