package com.szymong.trip_planner_api.trip.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.trip.mapper.TripMapper;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

  private final TripRepository tripRepository;
  private final UserRepository userRepository;
  private final TripMapper tripMapper;

  public TripServiceImpl(TripRepository triprepository, UserRepository userRepository, TripMapper tripMapper) {
    this.tripRepository = triprepository;
    this.userRepository = userRepository;
    this.tripMapper = tripMapper;
  }

  @Override
  public TripResponse getTripById(Long id) {
    Optional<Trip> result = tripRepository.findById(id);

    if (result.isEmpty()) {
      throw new RuntimeException("trip id not found" + id);
    }

    return tripMapper.mapToResponse(result.get());
  }

  @Override
  public List<TripResponse> getTripsByCreatorId(Long creatorId) {
    return tripRepository.findByCreatorId(creatorId).stream().map(tripMapper::mapToResponse).toList();
  }

  @Override
  public List<TripResponse> getAllTrips() {
    return tripRepository.findAll().stream().map(tripMapper::mapToResponse).toList();
  }

  @Override
  public TripResponse createTrip(Long userId, Trip trip) {

    Optional<User> creator = userRepository.findById(userId);

    if (creator.isEmpty()) {
      throw new RuntimeException("Creator not found");
    }

    trip.setId(null);
    trip.setCreator(creator.get());

    return tripMapper.mapToResponse(tripRepository.save(trip));
  }

  @Override
  public TripResponse updateTrip(Long id, Trip updatedTrip) {

    Optional<Trip> result = tripRepository.findById(id);

    if (result.isEmpty()) {
      throw new RuntimeException("Trip not found");
    }

    Trip existingTrip = result.get();

    existingTrip.setTitle(updatedTrip.getTitle());
    existingTrip.setDescription(updatedTrip.getDescription());
    existingTrip.setOrigin(updatedTrip.getOrigin());
    existingTrip.setDestination(updatedTrip.getDestination());
    existingTrip.setStatus(updatedTrip.getStatus());
    existingTrip.setEstimatedDuration(updatedTrip.getEstimatedDuration());

    return tripMapper.mapToResponse(tripRepository.save(existingTrip));
  }

  @Override
  public void deleteTrip(Long id) {
    tripRepository.deleteById(id);
  }


}
