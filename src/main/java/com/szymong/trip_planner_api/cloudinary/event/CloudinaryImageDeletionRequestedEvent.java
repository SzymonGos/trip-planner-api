package com.szymong.trip_planner_api.cloudinary.event;

import java.util.List;

public record CloudinaryImageDeletionRequestedEvent(List<String> publicIds) {
  public CloudinaryImageDeletionRequestedEvent {
    publicIds = List.copyOf(publicIds);
  }
}
