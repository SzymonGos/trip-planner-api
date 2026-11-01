package com.szymong.trip_planner_api.aiChat.controller;

import com.szymong.trip_planner_api.aiChat.dto.AiChatRequest;
import com.szymong.trip_planner_api.aiChat.dto.AiChatResponse;
import com.szymong.trip_planner_api.aiChat.service.AiChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-chat")
public class AiChatController {

  private final AiChatService aiChatService;

  public AiChatController(AiChatService aiChatService) {
    this.aiChatService = aiChatService;
  }

  @PostMapping
  public AiChatResponse sendMessage(
          @Valid @RequestBody AiChatRequest request) {

    return aiChatService.sendMessage(request);
  }
}