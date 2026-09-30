package com.szymong.trip_planner_api.trip.service;

import com.szymong.trip_planner_api.trip.dto.CreateTripRequest;
import com.szymong.trip_planner_api.trip.dto.CreateTripResponse;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.trip.dto.UpdateTripRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TripService {

  TripResponse getTripById(Long id);

  Slice<TripResponse> getAllTrips(Pageable pageable);

  CreateTripResponse createTrip(CreateTripRequest request, List<MultipartFile> images);

  TripResponse updateTrip(Long id, UpdateTripRequest request, List<MultipartFile> images);

  void deleteTrip(Long id);


}
