package com.szymong.trip_planner_api.user.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.user.User;

import java.util.List;

public interface UserService {
  User getUserById(Long id);

  List<Trip> getUserTrips(Long id);

  User getCurrentUser();

  List<Trip> getCurrentUserTrips();

  User getUserByClerkId(String clerkId);
}
