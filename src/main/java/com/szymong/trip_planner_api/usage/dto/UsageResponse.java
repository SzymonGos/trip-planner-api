package com.szymong.trip_planner_api.usage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UsageResponse {

  private Integer googleMapsRouteCount;

  private Integer googleMapsMaxLimit;

  private LocalDateTime googleMapsRouteResetDate;
}
