package com.szymong.trip_planner_api.user.service;

import com.szymong.trip_planner_api.exceptions.ResourceNotFoundException;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.trip.mapper.TripMapper;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.dto.*;
import com.szymong.trip_planner_api.user.mapper.UserMapper;
import com.szymong.trip_planner_api.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final TripRepository tripRepository;
  private final UserMapper userMapper;
  private final TripMapper tripMapper;

  public UserServiceImpl(UserRepository userRepository, TripRepository tripRepository, UserMapper userMapper, TripMapper tripMapper) {
    this.userRepository = userRepository;
    this.tripRepository = tripRepository;
    this.userMapper = userMapper;
    this.tripMapper = tripMapper;
  }

  public UserResponse getUserById(Long id) {
    Optional<User> result = userRepository.findById(id);

    if (result.isEmpty()) {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }

    return userMapper.mapToResponse(result.get());
  }

  @Override
  public UserResponse getUserByUsername(String username) {
    Optional<User> result = userRepository.findByUsername(username);

    if (result.isEmpty()) {
      throw new ResourceNotFoundException("User not found with username: " + username);
    }

    return userMapper.mapToResponse(result.get());
  }

  @Override
  public List<TripResponse> getUserTrips(Long id) {
    User user = findUserById(id);

    return tripRepository.findByCreatorId(user.getId()).stream().map(tripMapper::mapToResponse).toList();
  }

  @Override
  public CurrentUserResponse getCurrentUser() {
    User user = getAuthenticatedUser();

    return new CurrentUserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImagePublicId());
  }

  @Override
  public List<TripResponse> getCurrentUserTrips() {
    CurrentUserResponse currentUser = getCurrentUser();

    return tripRepository.findByCreatorId(currentUser.getId()).stream().map(tripMapper::mapToResponse).toList();
  }

  public User getUserByClerkId(String clerkId) {
    Optional<User> result = userRepository.findByClerkId(clerkId);
    if (result.isPresent()) {
      return result.get();
    } else {
      throw new ResourceNotFoundException("User not found with clerkId: " + clerkId);
    }
  }
  @Override
  public CreateUserResponse createUser(CreateUserRequest request) {

    Optional<User> existingUser = userRepository.findByClerkId(request.getClerkId());

    if (existingUser.isPresent()) {
      return userMapper.mapToCreateUserResponse(existingUser.get());
    }

    User newUser = new User();

    newUser.setClerkId(request.getClerkId());
    newUser.setUsername(request.getUsername());
    newUser.setEmail(request.getEmail());


    return userMapper.mapToCreateUserResponse(userRepository.save(newUser));
  }

  @Override
  public UpdateCurrentUserResponse updateUser(UpdateCurrentUserRequest request) {
    User user = getAuthenticatedUser();

    user.setUsername(request.getUsername());
    user.setProfileImagePublicId(request.getProfileImagePublicId());
    User updatedUser = userRepository.save(user);

    return userMapper.mapToUpdateCurrentUserResponse(updatedUser);
  }

  private User findUserById(Long id) {
    return userRepository.findById(id)
                   .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  private User getAuthenticatedUser(){
    Authentication authentication = SecurityContextHolder
                                            .getContext()
                                            .getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("Authenticated user is required");
    }

    String clerkId = authentication.getName();

    return getUserByClerkId(clerkId);
  }

}
