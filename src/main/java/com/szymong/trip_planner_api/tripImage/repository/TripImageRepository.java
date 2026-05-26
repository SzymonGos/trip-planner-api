package com.szymong.trip_planner_api.tripImage.repository;

import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.dto.TripImageResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripImageRepository extends JpaRepository<TripImage, Long> {
  List<TripImage> findByTripId(Long tripId);
}
