package com.szymong.trip_planner_api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateCurrentUserRequest {
  private Long id;

  private String username;

  private String profileImagePublicId;
}
