package com.szymong.trip_planner_api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserResponse {

  private Long id;

  private String clerkId;

  private String username;

  private String email;
}
