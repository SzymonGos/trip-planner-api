package com.szymong.trip_planner_api.tripImage.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.dto.TripImageResponse;
import com.szymong.trip_planner_api.tripImage.mapper.TripImageMapper;
import com.szymong.trip_planner_api.tripImage.repository.TripImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripImageServiceImpl implements TripImageService {

  private final TripImageRepository tripImageRepository;
  private final TripRepository tripRepository;
  private final TripImageMapper tripImageMapper;

  public TripImageServiceImpl(TripImageRepository tripImageRepository, TripRepository tripRepository, TripImageMapper tripImageMapper) {
    this.tripImageRepository = tripImageRepository;
    this.tripRepository = tripRepository;
    this.tripImageMapper = tripImageMapper;
  }

  @Override
  public TripImageResponse getTripImageById(Long id) {
    Optional<TripImage> result = tripImageRepository.findById(id);

    if (result.isEmpty()) {
      throw new RuntimeException("Trip image not found");
    }

    return tripImageMapper.mapToResponse(result.get());
  }

  @Override
  public List<TripImageResponse> getTripImagesByTripId(Long tripId) {
    return tripImageRepository.findByTripId(tripId).stream()
                   .map(tripImageMapper::mapToResponse)
                   .toList();
  }

  @Override
  public TripImage createTripImage(Long tripId, TripImage tripImage) {

    Optional<Trip> trip = tripRepository.findById(tripId);

    if (trip.isEmpty()) {
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
