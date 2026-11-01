package com.szymong.trip_planner_api.aiChat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenAiConfig {

  @Bean
  public RestClient openAiRestClient(
          RestClient.Builder builder,
          @Value("${openai.api-key}") String apiKey) {

    return builder
                   .baseUrl("https://api.openai.com/v1")
                   .defaultHeader(
                           HttpHeaders.AUTHORIZATION,
                           "Bearer " + apiKey
                   )
                   .build();
  }
}