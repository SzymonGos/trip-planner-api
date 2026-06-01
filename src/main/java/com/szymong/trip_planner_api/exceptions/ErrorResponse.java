package com.szymong.trip_planner_api.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class ErrorResponse {

  private int status;
  private String message;
  private String path;
  private LocalDateTime timestamp;
}
