package com.szymong.trip_planner_api.trip.dto;

import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.tripImage.TripImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

  private TripCreatorResponse creator;

  private Long estimatedDurationSeconds;

  private TripStatus status;

  private LocalDateTime createdAt;

  private List<TripImage> tripImages;
}
