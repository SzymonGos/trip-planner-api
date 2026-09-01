package com.szymong.trip_planner_api.trip.dto;

import com.szymong.trip_planner_api.trip.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CreateTripRequest {

  private String title;

  private String description;

  private String origin;

  private String destination;

  private TripStatus status;

  private LocalDateTime createdAt;

}
