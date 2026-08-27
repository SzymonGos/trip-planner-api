package com.szymong.trip_planner_api.usage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "app.usage")
public class UsageProperties {

  public GoogleMaps googleMaps;

  @Getter
  @Setter
  public static class GoogleMaps {
    private int maxLimit;
  }
}