package com.szymong.trip_planner_api.usage.service;

import com.szymong.trip_planner_api.exceptions.GoogleMapsUsageLimitExceededException;
import com.szymong.trip_planner_api.usage.config.UsageProperties;
import com.szymong.trip_planner_api.usage.dto.UsageResponse;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.repository.UserRepository;
import com.szymong.trip_planner_api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsageServiceImplTest {
  @Mock
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UsageProperties usageProperties;

  @InjectMocks
  private UsageServiceImpl usageService;


  @Test
  void shouldIncrementGoogleMapsUsageWhenBelowLimit() {
    User user = new User();
    user.setGoogleMapsRouteCount(5);

    UsageProperties.GoogleMaps googleMaps = mock(UsageProperties.GoogleMaps.class);

    when(usageProperties.getGoogleMaps()).thenReturn(googleMaps);

    when(googleMaps.getMaxLimit()).thenReturn(10);

    usageService.incrementGoogleMapsUsage(user);

    assertThat(user.getGoogleMapsRouteCount()).isEqualTo(6);

    verify(userRepository).save(user);
  }

  @Test
  void shouldThrowExceptionWhenLimitReached() {
    User user = new User();
    user.setGoogleMapsRouteCount(10);

    UsageProperties.GoogleMaps googleMaps = mock(UsageProperties.GoogleMaps.class);

    when(usageProperties.getGoogleMaps()).thenReturn(googleMaps);

    when(googleMaps.getMaxLimit()).thenReturn(10);

    assertThatThrownBy(() -> usageService.incrementGoogleMapsUsage(user)).isInstanceOf(GoogleMapsUsageLimitExceededException.class);

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void shouldReturnCurrentUserUsage() {
    User user = new User();
    user.setGoogleMapsRouteCount(5);

    UsageProperties.GoogleMaps googleMaps = mock(UsageProperties.GoogleMaps.class);

    when(userService.getAuthenticatedUser()).thenReturn(user);
    when(usageProperties.getGoogleMaps()).thenReturn(googleMaps);

    when(googleMaps.getMaxLimit()).thenReturn(10);

    UsageResponse result = usageService.getUserUsage();

    assertThat(result.getGoogleMapsRouteCount()).isEqualTo(5);
  }


  @Test
  void shouldIncrementGoogleMapsUsageWhenOneBelowLimit() {
    User user = new User();
    user.setGoogleMapsRouteCount(9);

    UsageProperties.GoogleMaps googleMaps = mock(UsageProperties.GoogleMaps.class);

    when(usageProperties.getGoogleMaps()).thenReturn(googleMaps);
    when(googleMaps.getMaxLimit()).thenReturn(10);

    usageService.incrementGoogleMapsUsage(user);

    assertThat(user.getGoogleMapsRouteCount()).isEqualTo(10);

    verify(userRepository).save(user);
  }

  @Test
  void shouldNotIncrementUsageWhenLimitAlreadyExceeded() {
    User user = new User();
    user.setGoogleMapsRouteCount(12);

    UsageProperties.GoogleMaps googleMaps = mock(UsageProperties.GoogleMaps.class);

    when(usageProperties.getGoogleMaps()).thenReturn(googleMaps);
    when(googleMaps.getMaxLimit()).thenReturn(10);

    assertThatThrownBy(() -> usageService.incrementGoogleMapsUsage(user)).isInstanceOf(GoogleMapsUsageLimitExceededException.class);

    assertThat(user.getGoogleMapsRouteCount()).isEqualTo(12);

    verify(userRepository, never()).save(any(User.class));
  }
}
