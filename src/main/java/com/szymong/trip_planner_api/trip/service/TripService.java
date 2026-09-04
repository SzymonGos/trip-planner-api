package com.szymong.trip_planner_api.trip.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.dto.CreateTripRequest;
import com.szymong.trip_planner_api.trip.dto.CreateTripResponse;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TripService {

  TripResponse getTripById(Long id);

  List<TripResponse> getTripsByCreatorId(Long creatorId);

  List<TripResponse> getAllTrips();

  CreateTripResponse createTrip(CreateTripRequest request, List<MultipartFile> images);

  TripResponse updateTrip(Long id, Trip updatedTrip);

  void deleteTrip(Long id);


}
