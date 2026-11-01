package com.szymong.trip_planner_api.aiChat.service;

import com.szymong.trip_planner_api.aiChat.client.OpenAiClient;
import com.szymong.trip_planner_api.aiChat.dto.AiChatRequest;
import com.szymong.trip_planner_api.aiChat.dto.AiChatResponse;
import com.szymong.trip_planner_api.usage.dto.UsageResponse;
import com.szymong.trip_planner_api.usage.service.UsageService;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AiChatServiceImpl implements AiChatService {

  private final OpenAiClient openAiClient;
  private final UsageService usageService;
  private final UserService userService;

  public AiChatServiceImpl(
          OpenAiClient openAiClient,
          UsageService usageService,
          UserService userService) {

    this.openAiClient = openAiClient;
    this.usageService = usageService;
    this.userService = userService;
  }

  @Override
  public AiChatResponse sendMessage(AiChatRequest request) {

    User user = userService.getAuthenticatedUser();

    usageService.incrementAiChatUsage(user);

    String aiResponse = openAiClient.sendMessage(
            request.getMessage()
    );

    UsageResponse usage = usageService.getUserUsage();

    return new AiChatResponse(
            aiResponse,
            usage.getAiChatUsageCount(),
            usage.getAiChatMaxLimit()
    );
  }
}