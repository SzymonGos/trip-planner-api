package com.szymong.trip_planner_api.user.service;

import com.szymong.trip_planner_api.exceptions.ResourceNotFoundException;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.trip.mapper.TripMapper;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.dto.UserResponse;
import com.szymong.trip_planner_api.user.mapper.UserMapper;
import com.szymong.trip_planner_api.user.repository.UserRepository;
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
  public List<TripResponse> getUserTrips(Long id) {
    User user = findUserById(id);

    return tripRepository.findByCreatorId(user.getId()).stream().map(tripMapper::mapToResponse).toList();
  }

  @Override
  public User getCurrentUser() {
    // todo: after clerk integration use getUserByClerkIds

    throw new UnsupportedOperationException("Current user requires JWT/Clerk integration");
  }

  @Override
  public List<TripResponse> getCurrentUserTrips() {
    User currentUser = getCurrentUser();

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

  private User findUserById(Long id) {
    return userRepository.findById(id)
                   .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }
}
