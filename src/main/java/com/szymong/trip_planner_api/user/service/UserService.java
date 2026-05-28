package com.szymong.trip_planner_api.user.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.dto.UserResponse;

import java.util.List;

public interface UserService {
  UserResponse getUserById(Long id);

  List<TripResponse> getUserTrips(Long id);

  User getCurrentUser();

  List<TripResponse> getCurrentUserTrips();

  User getUserByClerkId(String clerkId);
}
