package com.szymong.trip_planner_api.trip.service;

import com.szymong.trip_planner_api.exceptions.ResourceNotFoundException;
import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.trip.dto.CreateTripRequest;
import com.szymong.trip_planner_api.trip.dto.CreateTripResponse;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.trip.dto.UpdateTripRequest;
import com.szymong.trip_planner_api.trip.mapper.TripMapper;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.tripImage.service.TripImageService;
import com.szymong.trip_planner_api.usage.service.UsageService;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
  public Slice<TripResponse> getAllTrips(Pageable pageable) {
    return tripRepository.findAll(pageable).map(tripMapper::mapToResponse);
  }

  @Override
  @Transactional
  public CreateTripResponse createTrip(CreateTripRequest request, List<MultipartFile> images) {
    User user = userService.getAuthenticatedUser();

    if (request.getStatus() == TripStatus.PLANNING && hasImages(images)) {
      throw new IllegalArgumentException("A trip in PLANNING status cannot have images");
    }

    usageService.incrementGoogleMapsUsage(user);

    Trip newTrip = new Trip();

    newTrip.setCreator(user);
    newTrip.setTitle(request.getTitle());
    newTrip.setDescription(request.getDescription());
    newTrip.setOrigin(request.getOrigin());
    newTrip.setDestination(request.getDestination());
    newTrip.setStatus(request.getStatus());
    newTrip.setEstimatedDurationSeconds(request.getEstimatedDurationSeconds());
    newTrip.setDistanceMeters(request.getDistanceMeters());

    Trip savedTrip = tripRepository.save(newTrip);

    tripImageService.addTripImages(savedTrip, images);

    return tripMapper.mapToCreateResponse(savedTrip);
  }

  @Override
  @Transactional
  public TripResponse updateTrip(Long id, UpdateTripRequest request, List<MultipartFile> images) {
    User user = userService.getAuthenticatedUser();

    Trip existingTrip = tripRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));

    if (!Objects.equals(user.getId(), existingTrip.getCreator().getId())) {
      throw new AccessDeniedException("You are not allowed to update this trip");
    }

    boolean routeChanged = !Objects.equals(existingTrip.getOrigin(), request.getOrigin()) || !Objects.equals(existingTrip.getDestination(), request.getDestination());

    if (routeChanged) {
      usageService.incrementGoogleMapsUsage(user);
    }

    if (request.getStatus() == TripStatus.PLANNING) {
      Set<Long> removedIds = request.getRemovedImageIds() == null ? Set.of() : new HashSet<>(request.getRemovedImageIds());

      boolean existingImagesWillRemain = existingTrip.getTripImages().stream().anyMatch(image -> !removedIds.contains(image.getId()));

      if (existingImagesWillRemain || hasImages(images)) {
        throw new IllegalArgumentException("Remove all images before changing the trip status to PLANNING");
      }
    }

    existingTrip.setTitle(request.getTitle());
    existingTrip.setDescription(request.getDescription());
    existingTrip.setOrigin(request.getOrigin());
    existingTrip.setDestination(request.getDestination());
    existingTrip.setStatus(request.getStatus());
    existingTrip.setDistanceMeters(request.getDistanceMeters());
    existingTrip.setEstimatedDurationSeconds(request.getEstimatedDurationSeconds());

    tripImageService.removeTripImages(existingTrip, request.getRemovedImageIds());

    tripImageService.addTripImages(existingTrip, images);

    return tripMapper.mapToResponse(tripRepository.save(existingTrip));
  }

  @Override
  public void deleteTrip(Long id) {
    tripRepository.deleteById(id);
  }

  private boolean hasImages(List<MultipartFile> images) {
    return images != null && images.stream().anyMatch(image -> image != null && !image.isEmpty());
  }
}
