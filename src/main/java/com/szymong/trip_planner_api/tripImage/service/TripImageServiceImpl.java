package com.szymong.trip_planner_api.tripImage.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.repository.TripImageRepository;

import java.util.List;
import java.util.Optional;

public class TripImageServiceImpl implements TripImageService {

  private final TripImageRepository tripImageRepository;
  private final TripRepository tripRepository;

  public TripImageServiceImpl(TripImageRepository tripImageRepository, TripRepository tripRepository) {
    this.tripImageRepository = tripImageRepository;
    this.tripRepository = tripRepository;
  }

  @Override
  public TripImage getTripImageById(Long id) {
    Optional<TripImage> result = tripImageRepository.findById(id);

    if(result.isEmpty()){
      throw new RuntimeException("Trip image not found");
    }

    return result.get();
  }

  @Override
  public List<TripImage> getTripImagesByTripId(Long tripId) {
    return tripImageRepository.findByTripId(tripId);
  }

  @Override
  public TripImage createTripImage(Long tripId, TripImage tripImage) {

    Optional<Trip> trip = tripRepository.findById(tripId);

    if (trip.isEmpty()){
      throw new RuntimeException("Trip not found");
    }

    tripImage.setId(null);
    tripImage.setTrip(trip.get());

    return tripImageRepository.save(tripImage);
  }

  @Override
  public void deleteTripImage(Long id) {
    tripImageRepository.deleteById(id);
  }
}
