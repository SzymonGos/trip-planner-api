package com.szymong.trip_planner_api.tripImage.controller;

import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.service.TripImageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-images")
public class TripImageController {

  public final TripImageService tripImageService;

  public TripImageController(TripImageService tripImageService) {
    this.tripImageService = tripImageService;
  }

  @GetMapping("/{id}")
  public TripImage getTripImageById(@PathVariable Long id) {
    return tripImageService.getTripImageById(id);
  }

  @GetMapping
  public List<TripImage> getTripImagesByTripId(@RequestParam Long tripId) {
    return tripImageService.getTripImagesByTripId(tripId);
  }

  @PostMapping
  public TripImage createTripImage(@RequestParam Long tripId, @RequestBody TripImage tripImage) {
    return tripImageService.createTripImage(tripId, tripImage);
  }

  @DeleteMapping("/{id}")
  public void deleteTripImage(@PathVariable Long id) {
    tripImageService.deleteTripImage(id);
  }
}
