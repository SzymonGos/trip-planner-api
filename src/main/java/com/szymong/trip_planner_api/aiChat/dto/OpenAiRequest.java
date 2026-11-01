package com.szymong.trip_planner_api.aiChat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class OpenAiRequest {

  private String model;
  private List<Message> input;
  private Map<String, String> reasoning;
  private List<Map<String, String>> tools;
  private boolean stream;
  private boolean store;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Message {
    private String role;
    private String content;
  }
}