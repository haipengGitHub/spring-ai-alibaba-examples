/*
 * Copyright 2024-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hai.ollama.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * * @author Christian Tzolov
 */
@Service
public class DailyReportAssistant {

	private final ChatClient chatClient;

	public DailyReportAssistant(ChatClient.Builder modelBuilder, ChatMemory chatMemory) {

		// @formatter:off
		this.chatClient = modelBuilder
				.defaultSystem("""
						  您是一个善于记录日报和总结周报的好助手。请以友好、乐于助人且愉快的方式来回复。
						  您正在通过在线聊天系统与客户互动。
						  您能够支持记录日报、总结周报等操作，如果用户问的问题不支持请告知详情。
						  如果需要，您可以调用相应函数辅助完成。
						  请讲中文。
						  今天的日期是 {current_date}.
				""")
				.defaultAdvisors(
						new PromptChatMemoryAdvisor(chatMemory), // Chat Memory
						// new VectorStoreChatMemoryAdvisor(vectorStore)),
					
//						new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()), // RAG
						// new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()
						// 	.withFilterExpression("'documentType' == 'terms-of-service' && region in ['EU', 'US']")),
						
						new LoggingAdvisor())
						
				.defaultFunctions("recordDailyReport", "summaryWeeklyReport") // FUNCTION CALLING

				.build();
		// @formatter:on
	}

	public Flux<String> chat(String chatId, String userMessageContent) {

		return this.chatClient.prompt()
			.system(s -> s.param("current_date", LocalDate.now().toString()))
			.user(userMessageContent)
			.advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
			.stream()
			.content();
	}

}