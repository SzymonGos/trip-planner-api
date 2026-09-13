package com.szymong.trip_planner_api.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TripCreatorResponse {

  private Long id;

  private String username;

  private String profileImagePublicId;
}
