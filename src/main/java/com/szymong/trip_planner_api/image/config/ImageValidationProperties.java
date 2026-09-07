package com.szymong.trip_planner_api.image.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.util.Set;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.images")
public class ImageValidationProperties {

  private DataSize maxFileSize;
  private Set<String> allowedContentTypes;
}
