package com.szymong.trip_planner_api.usage.service;

import com.szymong.trip_planner_api.exceptions.AiChatUsageLimitExceededException;
import com.szymong.trip_planner_api.exceptions.GoogleMapsUsageLimitExceededException;
import com.szymong.trip_planner_api.usage.config.UsageProperties;
import com.szymong.trip_planner_api.usage.dto.UsageResponse;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.repository.UserRepository;
import com.szymong.trip_planner_api.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    return new UsageResponse(
            user.getGoogleMapsRouteCount(),
            usageProperties.getGoogleMaps().getMaxLimit(),
            user.getGoogleMapsRouteResetDate(),

            user.getAiChatUsageCount(),
            usageProperties.getAiChat().getMaxLimit(),
            user.getAiChatUsageResetDate()
            );
  }

  public void incrementGoogleMapsUsage(User user) {
    int maxLimit = usageProperties.getGoogleMaps().getMaxLimit();

    if(user.getGoogleMapsRouteCount() < maxLimit){
      user.setGoogleMapsRouteCount(user.getGoogleMapsRouteCount() + 1);
      userRepository.save(user);
    } else {
      throw new GoogleMapsUsageLimitExceededException("User reached max limit of Google Maps Routes: " + maxLimit);
    }
  }

  @Override
  @Transactional
  public void incrementAiChatUsage(User user) {
    User managedUser = userRepository.findByIdForUpdate(user.getId())
                               .orElseThrow(() ->
                                                    new IllegalStateException("User not found"));

    LocalDateTime now = LocalDateTime.now();

    if (!now.isBefore(managedUser.getAiChatUsageResetDate())) {
      managedUser.setAiChatUsageCount(0);
      managedUser.setAiChatUsageResetDate(now.plusMonths(1));
    }

    int maxLimit = usageProperties.getAiChat().getMaxLimit();

    if (managedUser.getAiChatUsageCount() >= maxLimit) {
      throw new AiChatUsageLimitExceededException(
              "User reached max limit of AI Chat messages: " + maxLimit
      );
    }

    managedUser.setAiChatUsageCount(
            managedUser.getAiChatUsageCount() + 1
    );
  }

}
