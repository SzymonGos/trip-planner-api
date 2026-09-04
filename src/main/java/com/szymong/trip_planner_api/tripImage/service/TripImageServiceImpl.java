package com.szymong.trip_planner_api.tripImage.service;

import com.szymong.trip_planner_api.cloudinary.service.CloudinaryService;
import com.szymong.trip_planner_api.exceptions.ResourceNotFoundException;
import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.config.TripImageProperties;
import com.szymong.trip_planner_api.tripImage.dto.TripImageResponse;
import com.szymong.trip_planner_api.tripImage.mapper.TripImageMapper;
import com.szymong.trip_planner_api.tripImage.repository.TripImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class TripImageServiceImpl implements TripImageService {

  private final TripImageRepository tripImageRepository;
  private final TripRepository tripRepository;
  private final TripImageMapper tripImageMapper;
  private final TripImageProperties tripImageProperties;
  private final CloudinaryService cloudinaryService;

  public TripImageServiceImpl(TripImageRepository tripImageRepository, TripRepository tripRepository, TripImageMapper tripImageMapper, TripImageProperties tripImageProperties, CloudinaryService cloudinaryService) {
    this.tripImageRepository = tripImageRepository;
    this.tripRepository = tripRepository;
    this.tripImageMapper = tripImageMapper;
    this.tripImageProperties = tripImageProperties;
    this.cloudinaryService = cloudinaryService;
  }

  @Override
  public TripImageResponse getTripImageById(Long id) {
    Optional<TripImage> result = tripImageRepository.findById(id);

    if (result.isEmpty()) {
      throw new ResourceNotFoundException("Trip image not found with id: " + id);
    }

    return tripImageMapper.mapToResponse(result.get());
  }

  @Override
  public List<TripImageResponse> getTripImagesByTripId(Long tripId) {
    return tripImageRepository.findByTripId(tripId).stream()
                   .map(tripImageMapper::mapToResponse)
                   .toList();
  }

  @Override
  public TripImage createTripImage(Long tripId, TripImage tripImage) {

    Optional<Trip> trip = tripRepository.findById(tripId);

    if (trip.isEmpty()) {
      throw new ResourceNotFoundException("Trip not found with id: " + tripId);
    }

    tripImage.setId(null);
    tripImage.setTrip(trip.get());

    return tripImageRepository.save(tripImage);
  }

  @Override
  public void deleteTripImage(Long id) {
    tripImageRepository.deleteById(id);
  }

  public void addTripImages(Trip trip, List<MultipartFile> images) {
    if (images == null || images.isEmpty()) {
      return;
    }
    if (trip.getStatus() != TripStatus.COMPLETED) {
      // todo: change the runntime error - temp for now
      throw new RuntimeException("Trip is not completed yet");
    }
    if (trip.getTripImages().size() + images.size() > tripImageProperties.getMaxLimit()) {
      // todo: change the runntime error - temp for now
      throw new RuntimeException("Limit is reached");
    }
    // verify image max weight
    // verify image format
    for(MultipartFile image: images){
      String publicId = cloudinaryService.uploadTripImage(image);

      TripImage newTripImage = new TripImage();

      newTripImage.setPublicId(publicId);
      newTripImage.setTrip(trip);

      tripImageRepository.save(newTripImage);
      trip.getTripImages().add(newTripImage);
    }
  }
}
