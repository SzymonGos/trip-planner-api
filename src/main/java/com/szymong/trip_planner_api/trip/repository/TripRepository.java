package com.szymong.trip_planner_api.trip.repository;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
  List<Trip> findByCreatorId(Long creatorId);
}
