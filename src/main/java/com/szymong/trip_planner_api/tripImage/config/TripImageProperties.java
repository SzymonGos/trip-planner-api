package com.szymong.trip_planner_api.tripImage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.trip-images")
public class TripImageProperties {
  private int maxLimit;
}
