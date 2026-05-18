package com.szymong.trip_planner_api.tripImage.service;

import com.szymong.trip_planner_api.tripImage.TripImage;

import java.util.List;

public interface TripImageService {
  TripImage getTripImageById(Long id);

  List<TripImage> getTripImagesByTripId(Long tripId);

  TripImage createTripImage(Long tripId, TripImage tripImage);

  void deleteTripImage(Long id);
}
