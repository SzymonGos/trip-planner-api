package com.szymong.trip_planner_api.trip.dto;

import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.tripImage.TripImage;

import java.time.LocalDateTime;
import java.util.List;

public class CreateTripResponse {
  private String title;

  private String description;

  private String origin;

  private String destination;

  private TripStatus status;

  private LocalDateTime createdAt;

  private List<TripImage> tripImages;
}
