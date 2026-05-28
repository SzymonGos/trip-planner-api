package com.szymong.trip_planner_api.user.dto;

import com.szymong.trip_planner_api.trip.dto.TripResponse;

import java.util.List;

public class UserResponse {
  private Long id;

  private String username;

  private String profileImageUrl;

  private List<TripResponse> userTrips;
}
