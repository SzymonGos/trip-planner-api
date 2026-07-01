package com.szymong.trip_planner_api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserRequest {

  private String username;

  private String clerkId;

  private String email;
}
