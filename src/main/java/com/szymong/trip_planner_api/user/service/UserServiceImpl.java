package com.szymong.trip_planner_api.user.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final TripRepository tripRepository;

  public UserServiceImpl(UserRepository userRepository, TripRepository tripRepository) {
    this.userRepository = userRepository;
    this.tripRepository = tripRepository;
  }

  //todo: improve error handling
  public User getUserById(Long id) {
    Optional<User> result = userRepository.findById(id);

    if (result.isPresent()) {
      return result.get();
    } else {
      throw new RuntimeException("id not found");
    }
  }

  @Override
  public List<Trip> getUserTrips(Long id) {
    User user = getUserById(id);

    return tripRepository.findByCreatorId(user.getId());
  }

  @Override
  public User getCurrentUser() {
    // todo: after clerk integration use getUserByClerkIds

    throw new UnsupportedOperationException("Current user requires JWT/Clerk integration");
  }

  @Override
  public List<Trip> getCurrentUserTrips() {
    User currentUser = getCurrentUser();

    return tripRepository.findByCreatorId(currentUser.getId());
  }

  public User getUserByClerkId(String clerkId) {
    Optional<User> result = userRepository.findByClerkId(clerkId);

    if (result.isPresent()) {
      return result.get();
    } else {
      throw new RuntimeException("clerkId not found" + clerkId);
    }
  }
}
