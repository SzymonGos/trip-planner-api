package com.szymong.trip_planner_api.aiChat.client;

import com.szymong.trip_planner_api.aiChat.dto.OpenAiRequest;
import com.szymong.trip_planner_api.aiChat.dto.OpenAiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OpenAiClient {

  private final RestClient restClient;
  private final String model;

  public OpenAiClient(
          @Qualifier("openAiRestClient") RestClient restClient,
          @Value("${openai.model}") String model) {
    this.restClient = restClient;
    this.model = model;
  }

  public String sendMessage(String message) {

    OpenAiRequest request = new OpenAiRequest(
            model,
            List.of(
                    new OpenAiRequest.Message(
                            "developer",
                            """
                                    You are RouteTripper, an AI road trip planning assistant.
                                    
                                    Help users create practical and realistic road trips.
                                    
                                    When the user requests a trip plan, respond using
                                    the following Markdown structure:
                                    
                                    **Title:** Short, descriptive trip title
                                    **Origin:** Starting location
                                    **Destination:** Final location
                                    
                                    **Description:**
                                    
                                    Start with a short, engaging introduction (1-2 sentences)
                                    describing the journey and its main appeal.
                                    
                                    - **Place name** – One short sentence explaining why
                                      it is worth visiting.
                                    - Recommend 3-4 attractions or scenic stops.
                                    - Prioritize places along the actual route and
                                      near the destination.
                                    
                                    Rules:
                                    - Keep the entire description under 350 characters,
                                      including the introduction and highlights.
                                    - Use concise Markdown bullet points.
                                    - Avoid long paragraphs, numbered lists and
                                      unnecessary conclusions.
                                    - Do not invent distances or travel durations.
                                    - Do not suggest significant detours unless requested.
                                    - Only specify an origin and destination when
                                      provided by the user or clearly established.
                                    - If a required location is missing, ask the user
                                      for clarification rather than inventing one.
                                    - For general questions unrelated to trip planning,
                                      respond naturally without the trip plan structure.
                                    """
                    ),
                    new OpenAiRequest.Message("user", message)
            ),
            Map.of(),
            List.of(),
            false,
            true
    );

    OpenAiResponse response = restClient.post()
                                      .uri("/responses")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .body(request)
                                      .retrieve()
                                      .body(OpenAiResponse.class);

    if (response == null || response.getOutput() == null) {
      throw new IllegalStateException("Invalid OpenAI response");
    }

    String content = response.getOutput().stream()
                             .filter(output -> "message".equals(output.getType()))
                             .filter(output -> output.getContent() != null)
                             .flatMap(output -> output.getContent().stream())
                             .filter(item -> "output_text".equals(item.getType()))
                             .map(OpenAiResponse.Content::getText)
                             .filter(Objects::nonNull)
                             .collect(Collectors.joining("\n"));

    if (content.isBlank()) {
      throw new IllegalStateException("Empty OpenAI response");
    }

    return content;
  }
}