package com.szymong.trip_planner_api.tripImage.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class TripImageResponse {

  private Long id;

  private String url;

  private String publicId;
}
