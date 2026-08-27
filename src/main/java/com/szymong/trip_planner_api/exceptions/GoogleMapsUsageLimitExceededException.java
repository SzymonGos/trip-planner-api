package com.szymong.trip_planner_api.exceptions;

public class GoogleMapsUsageLimitExceededException extends RuntimeException {
  public GoogleMapsUsageLimitExceededException(String message) {
    super(message);
  }
}
