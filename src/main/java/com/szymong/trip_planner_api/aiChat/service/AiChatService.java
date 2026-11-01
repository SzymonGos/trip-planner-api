package com.szymong.trip_planner_api.aiChat.service;

import com.szymong.trip_planner_api.aiChat.dto.AiChatRequest;
import com.szymong.trip_planner_api.aiChat.dto.AiChatResponse;

public interface AiChatService {

  AiChatResponse sendMessage(AiChatRequest request);
}