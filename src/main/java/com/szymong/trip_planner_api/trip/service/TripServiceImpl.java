package com.szymong.trip_planner_api.trip.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class TripServiceImpl implements TripService {

  private final TripRepository tripRepository;
  private final UserRepository userRepository;

  public TripServiceImpl(TripRepository triprepository, UserRepository userRepository) {
    this.tripRepository = triprepository;
    this.userRepository = userRepository;
  }

  @Override
  public Trip getTripById(Long id) {

    Optional <Trip> result = tripRepository.findById(id);

    if(result.isPresent()) {
      return result.get();
    } else {
      throw new RuntimeException("trip id not found" + id);
    }
  }

  @Override
  public List<Trip> getTripsByCreatorId(Long creatorId) {
    return tripRepository.findByCreatorId(creatorId);
  }

  @Override
  public List<Trip> getAllTrips() {
    return tripRepository.findAll();
  }

  @Override
  public Trip createTrip(Long userId, Trip trip) {

    Optional<User> creator = userRepository.findById(userId);

    if(creator.isEmpty()) {
      throw new RuntimeException("Creator not found");
    }

    trip.setId(null);
    trip.setCreator(creator.get());

    return tripRepository.save(trip);
  }

  @Override
  public Trip updateTrip(Long id, Trip updatedTrip) {

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

    return tripRepository.save(existingTrip);
  }

  @Override
  public void deleteTrip(Long id) {
    tripRepository.deleteById(id);
  }


}
