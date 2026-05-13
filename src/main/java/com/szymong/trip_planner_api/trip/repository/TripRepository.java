package com.szymong.trip_planner_api.trip.repository;

import com.szymong.trip_planner_api.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
