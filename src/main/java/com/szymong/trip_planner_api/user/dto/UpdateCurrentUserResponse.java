package com.szymong.trip_planner_api.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateCurrentUserResponse {
  private String username;
}
