package com.hai.ollama;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.ElasticsearchVectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class OllamaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OllamaApplication.class, args);
    }
    // In the real world, ingesting documents would often happen separately, on a CI
    // server or similar.
    /*@Bean
    CommandLineRunner ingestTermOfServiceToVectorStore(EmbeddingModel embeddingModel, VectorStore vectorStore,
                                                       @Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs) {
        return args -> {
            // Ingest the document into the vector store
            vectorStore.write(new TokenTextSplitter().transform(new TextReader(termsOfServiceDocs).read()));

            vectorStore.similaritySearch("Cancelling Bookings").forEach(doc -> {
//                logger.info("Similar Document: {}", doc.getContent());
            });
        };
    }*/

  /*  @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
//        return new ElasticsearchVectorStore(embeddingModel)
        return new SimpleVectorStore(embeddingModel);
    }*/
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}