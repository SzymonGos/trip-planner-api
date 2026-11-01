package com.szymong.trip_planner_api.aiChat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OpenAiResponse {

  private List<Output> output;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Output {
    private String type;
    private String role;
    private List<Content> content;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Content {
    private String type;
    private String text;
  }
}