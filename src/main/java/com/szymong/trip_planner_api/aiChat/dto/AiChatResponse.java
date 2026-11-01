package com.szymong.trip_planner_api.aiChat.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiChatResponse {
  private String response;
  private Integer usageCount;
  private Integer maxLimit;
}
