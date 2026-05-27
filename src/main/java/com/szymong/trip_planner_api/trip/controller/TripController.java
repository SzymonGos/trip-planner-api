package com.szymong.trip_planner_api.trip.controller;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.trip.service.TripService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

  private final TripService tripService;

  public TripController(TripService tripService) {
    this.tripService = tripService;
  }

  @GetMapping
  public List<TripResponse> getAllTrips() {
    return tripService.getAllTrips();
  }

  @GetMapping("/{id}")
  public TripResponse getTripById(@PathVariable Long id) {
    return tripService.getTripById(id);
  }

  // todo: replace with DTO, request param used for testing only
  @PostMapping
  public TripResponse createTrip(@RequestParam Long userId, @Valid @RequestBody Trip trip) {
    return tripService.createTrip(userId, trip);
  }

  @PutMapping("/{id}")
  public TripResponse updateTrip(@PathVariable Long id, @Valid @RequestBody Trip trip) {
    return tripService.updateTrip(id, trip);
  }

  @DeleteMapping("/{id}")
  public void deleteTrip(@PathVariable Long id) {
    tripService.deleteTrip(id);
  }

}
