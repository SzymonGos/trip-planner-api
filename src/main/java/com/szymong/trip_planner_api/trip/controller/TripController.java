package com.szymong.trip_planner_api.trip.controller;

import com.szymong.trip_planner_api.trip.dto.CreateTripRequest;
import com.szymong.trip_planner_api.trip.dto.CreateTripResponse;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.trip.dto.UpdateTripRequest;
import com.szymong.trip_planner_api.trip.service.TripService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

  private final TripService tripService;

  public TripController(TripService tripService) {
    this.tripService = tripService;
  }

  @GetMapping
  public Slice<TripResponse> getAllTrips(Pageable pageable) {
    return tripService.getAllTrips(pageable);
  }

  @GetMapping("/{id}")
  public TripResponse getTripById(@PathVariable Long id) {
    return tripService.getTripById(id);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public CreateTripResponse createTrip(@RequestPart CreateTripRequest request,  @RequestPart(value = "images", required = false) List<MultipartFile> images) {
    return tripService.createTrip(request, images);
  }

  @PutMapping("/{id}")
  public TripResponse updateTrip(@PathVariable Long id, @RequestPart UpdateTripRequest request,  @RequestPart(value = "images", required = false) List<MultipartFile> images) {
    return tripService.updateTrip(id, request, images);
  }

  @DeleteMapping("/{id}")
  public void deleteTrip(@PathVariable Long id) {
    tripService.deleteTrip(id);
  }

}
