package com.szymong.trip_planner_api.trip.dto;

import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.tripImage.dto.TripImageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class TripResponse {

  private Long id;

  private String title;

  private String description;

  private Long distanceMeters;

  private Long estimatedDurationSeconds;

  private TripStatus status;

  private LocalDateTime createdAt;

  private TripCreatorResponse creator;

  private List<TripImageResponse> tripImages;

}
