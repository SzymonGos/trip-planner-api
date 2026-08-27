package com.szymong.trip_planner_api.usage.service;

import com.szymong.trip_planner_api.exceptions.GoogleMapsUsageLimitExceededException;
import com.szymong.trip_planner_api.usage.config.UsageProperties;
import com.szymong.trip_planner_api.usage.dto.UsageResponse;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.repository.UserRepository;
import com.szymong.trip_planner_api.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {

  private final UserService userService;
  private final UserRepository userRepository;
  private final UsageProperties usageProperties;

  public UsageServiceImpl(UserService userService, UserRepository userRepository, UsageProperties usageProperties) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.usageProperties = usageProperties;
  }

  public UsageResponse getUserUsage(){
    User user = userService.getAuthenticatedUser();

    return new UsageResponse(user.getGoogleMapsRouteCount(),usageProperties.getGoogleMaps().getMaxLimit(), user.getGoogleMapsRouteResetDate());
  }

  public User incrementGoogleMapsUsage() {
    User user = userService.getAuthenticatedUser();

    int maxLimit = usageProperties.getGoogleMaps().getMaxLimit();

    if(user.getGoogleMapsRouteCount() < maxLimit){
      user.setGoogleMapsRouteCount(user.getGoogleMapsRouteCount() + 1);
      return userRepository.save(user);
    } else {
      throw new GoogleMapsUsageLimitExceededException("User reached max limit of Google Maps Routes: " + maxLimit);
    }
  }

}
