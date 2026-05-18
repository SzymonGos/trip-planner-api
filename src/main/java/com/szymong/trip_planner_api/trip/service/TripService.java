package com.szymong.trip_planner_api.trip.service;

import com.szymong.trip_planner_api.trip.Trip;

import java.util.List;

public interface TripService {

  Trip getTripById(Long id);

  List<Trip> getTripsByCreatorId(Long creatorId);

  List<Trip> getAllTrips();

  Trip createTrip(Long userId, Trip trip);

  Trip updateTrip(Long id, Trip updatedTrip);

  void deleteTrip(Long id);


}
