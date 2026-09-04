package com.szymong.trip_planner_api.trip.service;

import com.szymong.trip_planner_api.exceptions.ResourceNotFoundException;
import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.dto.CreateTripRequest;
import com.szymong.trip_planner_api.trip.dto.CreateTripResponse;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.trip.mapper.TripMapper;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.tripImage.service.TripImageService;
import com.szymong.trip_planner_api.usage.service.UsageService;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

  private final TripRepository tripRepository;
  private final TripMapper tripMapper;
  private final UserService userService;
  private final UsageService usageService;
  private final TripImageService tripImageService;

  public TripServiceImpl(TripRepository triprepository, TripMapper tripMapper, UserService userService, UsageService usageService, TripImageService tripImageService) {
    this.tripRepository = triprepository;
    this.tripMapper = tripMapper;
    this.userService = userService;
    this.usageService = usageService;
    this.tripImageService = tripImageService;
  }

  @Override
  public TripResponse getTripById(Long id) {
    Optional<Trip> result = tripRepository.findById(id);

    if (result.isEmpty()) {
      throw new ResourceNotFoundException("Trip not found with id: " + id);
    }

    return tripMapper.mapToResponse(result.get());
  }

  @Override
  public List<TripResponse> getTripsByCreatorId(Long creatorId) {
    return tripRepository.findByCreatorId(creatorId).stream().map(tripMapper::mapToResponse).toList();
  }

  @Override
  public List<TripResponse> getAllTrips() {
    return tripRepository.findAll().stream().map(tripMapper::mapToResponse).toList();
  }

  @Override
  @Transactional
  public CreateTripResponse createTrip(CreateTripRequest request, List<MultipartFile> images) {

    User user = userService.getAuthenticatedUser();
    usageService.incrementGoogleMapsUsage(user);

    Trip newTrip = new Trip();

    newTrip.setCreator(user);
    newTrip.setTitle(request.getTitle());
    newTrip.setDescription(request.getDescription());
    newTrip.setOrigin(request.getOrigin());
    newTrip.setDestination(request.getDestination());
    newTrip.setStatus(request.getStatus());

    Trip savedTrip = tripRepository.save(newTrip);

    tripImageService.addTripImages(savedTrip, images);

    return tripMapper.mapToCreateResponse(savedTrip);
  }

  @Override
  public TripResponse updateTrip(Long id, Trip updatedTrip) {

    Optional<Trip> result = tripRepository.findById(id);

    if (result.isEmpty()) {
      throw new ResourceNotFoundException("Trip not found with id: " + id);
    }

    Trip existingTrip = result.get();

    existingTrip.setTitle(updatedTrip.getTitle());
    existingTrip.setDescription(updatedTrip.getDescription());
    existingTrip.setOrigin(updatedTrip.getOrigin());
    existingTrip.setDestination(updatedTrip.getDestination());
    existingTrip.setStatus(updatedTrip.getStatus());
    existingTrip.setEstimatedDuration(updatedTrip.getEstimatedDuration());

    return tripMapper.mapToResponse(tripRepository.save(existingTrip));
  }

  @Override
  public void deleteTrip(Long id) {
    tripRepository.deleteById(id);
  }


}
