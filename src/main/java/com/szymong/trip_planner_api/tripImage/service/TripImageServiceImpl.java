package com.szymong.trip_planner_api.tripImage.service;

import com.szymong.trip_planner_api.cloudinary.event.CloudinaryImageDeletionRequestedEvent;
import com.szymong.trip_planner_api.cloudinary.service.CloudinaryService;
import com.szymong.trip_planner_api.exceptions.ResourceNotFoundException;
import com.szymong.trip_planner_api.image.config.ImageValidationProperties;
import com.szymong.trip_planner_api.image.validation.ImageFileValidator;
import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.config.TripImageProperties;
import com.szymong.trip_planner_api.tripImage.dto.TripImageResponse;
import com.szymong.trip_planner_api.tripImage.mapper.TripImageMapper;
import com.szymong.trip_planner_api.tripImage.repository.TripImageRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class TripImageServiceImpl implements TripImageService {

  private final TripImageRepository tripImageRepository;
  private final TripRepository tripRepository;
  private final TripImageMapper tripImageMapper;
  private final TripImageProperties tripImageProperties;
  private final CloudinaryService cloudinaryService;
  private final ImageFileValidator imageFileValidator;
  private final ApplicationEventPublisher eventPublisher;

  public TripImageServiceImpl(TripImageRepository tripImageRepository, TripRepository tripRepository, TripImageMapper tripImageMapper, TripImageProperties tripImageProperties, CloudinaryService cloudinaryService, ImageValidationProperties imageValidationProperties, ImageFileValidator imageFileValidator, ApplicationEventPublisher eventPublisher) {
    this.tripImageRepository = tripImageRepository;
    this.tripRepository = tripRepository;
    this.tripImageMapper = tripImageMapper;
    this.tripImageProperties = tripImageProperties;
    this.cloudinaryService = cloudinaryService;
    this.imageFileValidator = imageFileValidator;
    this.eventPublisher = eventPublisher;
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

    for (MultipartFile image : images) {
      imageFileValidator.validateImage(image);
    }

    for (MultipartFile image : images) {
      String publicId = cloudinaryService.uploadTripImage(image);

      TripImage newTripImage = new TripImage();

      newTripImage.setPublicId(publicId);
      newTripImage.setTrip(trip);

      tripImageRepository.save(newTripImage);
      trip.getTripImages().add(newTripImage);
    }
  }

  @Override
  @Transactional
  public void removeTripImages(Trip trip, List<Long> imageIds) {
    if (imageIds == null || imageIds.isEmpty()) {
      return;
    }

    Set<Long> uniqueImageIds = new HashSet<>(imageIds);

    if (uniqueImageIds.size() != imageIds.size()) {
      throw new IllegalArgumentException(
              "Duplicate trip image IDs are not allowed"
      );
    }

    List<TripImage> imagesToRemove = tripImageRepository.findAllById(uniqueImageIds);

    if (imagesToRemove.size() != uniqueImageIds.size()) {
      throw new ResourceNotFoundException(
              "One or more trip images were not found"
      );
    }

    boolean containsImageFromAnotherTrip = imagesToRemove.stream().anyMatch(image -> !Objects.equals(image.getTrip().getId(), trip.getId()));

    if (containsImageFromAnotherTrip) {
      throw new AccessDeniedException(
              "One or more images do not belong to this trip"
      );
    }

    List<String> publicIds = imagesToRemove.stream().map(TripImage::getPublicId).toList();

    tripImageRepository.deleteAll(imagesToRemove);

    trip.getTripImages().removeIf(image -> uniqueImageIds.contains(image.getId()));

    eventPublisher.publishEvent(new CloudinaryImageDeletionRequestedEvent(publicIds));

  }
}
