package com.szymong.trip_planner_api.user.service;

import com.szymong.trip_planner_api.user.User;

public interface UserService {
  User getUserById(Long id);

  User getUserByClerkId(String clerkId);
}
