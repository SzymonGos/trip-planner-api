package com.szymong.trip_planner_api.trip.dto;

import com.szymong.trip_planner_api.trip.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateTripRequest {
  private String title;

  private String description;

  private String origin;

  private String destination;

  private Long distanceMeters;

  private Long estimatedDurationSeconds;

  private TripStatus status;

  private List<Long> removedImageIds = new ArrayList<>();
}
