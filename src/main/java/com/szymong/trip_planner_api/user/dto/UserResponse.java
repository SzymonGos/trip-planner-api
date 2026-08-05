package com.szymong.trip_planner_api.user.dto;

import com.szymong.trip_planner_api.trip.dto.TripResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
  private Long id;

  private String username;

  private String profileImageUrl;

  private LocalDateTime createdAt;

  private List<TripResponse> userTrips;
}
