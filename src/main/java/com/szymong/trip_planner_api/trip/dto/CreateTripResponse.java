package com.szymong.trip_planner_api.trip.dto;

import com.szymong.trip_planner_api.trip.TripStatus;

import java.time.LocalDateTime;

public class CreateTripResponse {
  private String title;

  private String description;

  private String origin;

  private String destination;

  private TripStatus status;

  private LocalDateTime createdAt;
}
