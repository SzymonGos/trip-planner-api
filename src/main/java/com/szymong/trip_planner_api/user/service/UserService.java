package com.szymong.trip_planner_api.user.service;

import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.dto.*;
import com.szymong.trip_planner_api.user.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
  UserResponse getUserById(Long id);

  List<TripResponse> getUserTrips(Long id);

  CurrentUserResponse getCurrentUser();

  List<TripResponse> getCurrentUserTrips();

  User getUserByClerkId(String clerkId);

  CreateUserResponse createUser(CreateUserRequest user);

  UserResponse getUserByUsername(String username);

  UpdateCurrentUserResponse updateUser(UpdateCurrentUserRequest user, MultipartFile profileImage);

  User getAuthenticatedUser();
}
