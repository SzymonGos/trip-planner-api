package com.szymong.trip_planner_api.usage.service;

import com.szymong.trip_planner_api.usage.dto.UsageResponse;
import com.szymong.trip_planner_api.user.User;

public interface UsageService {
  UsageResponse getUserUsage();

  void incrementGoogleMapsUsage(User user);
}
