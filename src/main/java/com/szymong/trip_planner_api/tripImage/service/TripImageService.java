package com.szymong.trip_planner_api.tripImage.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.dto.TripImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface TripImageService {
  TripImageResponse getTripImageById(Long id);

  List<TripImageResponse> getTripImagesByTripId(Long tripId);

  TripImage createTripImage(Long tripId, TripImage tripImage);

  void addTripImages(Trip trip, List<MultipartFile> images);

  void deleteTripImage(Long id);
}
