package com.szymong.trip_planner_api.exceptions;

public class AiChatUsageLimitExceededException extends RuntimeException {
  public AiChatUsageLimitExceededException(String message) {
    super(message);
  }
}