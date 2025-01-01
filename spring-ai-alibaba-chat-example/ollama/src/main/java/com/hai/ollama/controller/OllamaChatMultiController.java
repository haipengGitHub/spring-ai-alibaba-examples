package com.hai.ollama.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.ollama.api.OllamaOptions.builder;

@RestController
@RequestMapping(value ="ollama")
public class OllamaChatMultiController {

    public OllamaChatMultiController(OllamaChatModel ollamaChatModel,ResourceLoader resourceLoader) {
        this.ollamaChatModel = ollamaChatModel;
        this.resourceLoader = resourceLoader;
    }
    private final OllamaChatModel ollamaChatModel;

    private ResourceLoader resourceLoader;
    @GetMapping("/llava/chat")
    public String simpleChat(@RequestParam(value = "message", defaultValue = "Explain what do you see on this picture?") String message,
                             @RequestParam(value = "path", defaultValue = "classpath:/images/girl.jpg") String path) {
        var userMessage = new UserMessage(message,
                new Media(MimeTypeUtils.IMAGE_JPEG, resourceLoader.getResource(path)));

        ChatResponse call = ollamaChatModel.call(new Prompt(userMessage,
                builder().withModel("llava:7b")));
        return call.getResult().getOutput().getContent();
    }
}
