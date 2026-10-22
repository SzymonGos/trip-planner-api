package com.szymong.trip_planner_api.trip.service;

import com.szymong.trip_planner_api.cloudinary.event.CloudinaryImageDeletionRequestedEvent;
import com.szymong.trip_planner_api.exceptions.ResourceNotFoundException;
import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.trip.dto.CreateTripRequest;
import com.szymong.trip_planner_api.trip.dto.CreateTripResponse;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.trip.dto.UpdateTripRequest;
import com.szymong.trip_planner_api.trip.mapper.TripMapper;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.service.TripImageService;
import com.szymong.trip_planner_api.usage.service.UsageService;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceImplTest {

  @Mock private TripRepository tripRepository;
  @Mock private TripMapper tripMapper;
  @Mock private UserService userService;
  @Mock private UsageService usageService;
  @Mock private TripImageService tripImageService;
  @Mock private ApplicationEventPublisher eventPublisher;

  @InjectMocks private TripServiceImpl tripService;

  private User user(long id) {
    User user = new User();
    user.setId(id);
    return user;
  }

  private Trip tripOwnedBy(User user) {
    Trip trip = new Trip();
    trip.setId(10L);
    trip.setCreator(user);
    trip.setOrigin("Warsaw");
    trip.setDestination("Krakow");
    trip.setStatus(TripStatus.COMPLETED);
    return trip;
  }

  @Test
  void getTripById_shouldReturnMappedTripWhenFound() {
    Trip trip = tripOwnedBy(user(1L));
    TripResponse expected = mock(TripResponse.class);
    when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));
    when(tripMapper.mapToResponse(trip)).thenReturn(expected);

    TripResponse result = tripService.getTripById(10L);

    assertThat(result).isSameAs(expected);
    verify(tripRepository).findById(10L);
    verify(tripMapper).mapToResponse(trip);
  }

  @Test
  void getTripById_shouldThrowWhenMissing() {
    when(tripRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> tripService.getTripById(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("99");
    verifyNoInteractions(tripMapper);
  }

  @Test
  void getAllTrips_shouldMapSlice() {
    Pageable pageable = PageRequest.of(0, 2);
    Trip first = tripOwnedBy(user(1L));
    Trip second = tripOwnedBy(user(2L));
    Page<Trip> repositoryPage = new PageImpl<>(List.of(first, second), pageable, 5);
    TripResponse firstResponse = mock(TripResponse.class);
    TripResponse secondResponse = mock(TripResponse.class);
    when(tripRepository.findAll(pageable)).thenReturn(repositoryPage);
    when(tripMapper.mapToResponse(first)).thenReturn(firstResponse);
    when(tripMapper.mapToResponse(second)).thenReturn(secondResponse);

    Slice<TripResponse> result = tripService.getAllTrips(pageable);

    assertThat(result.getContent()).containsExactly(firstResponse, secondResponse);
    assertThat(result.hasNext()).isTrue();
    verify(tripRepository).findAll(pageable);
  }

  @Test
  void createTrip_shouldSaveTripAndAddImages() {
    User creator = user(1L);
    CreateTripRequest request = mock(CreateTripRequest.class);
    CreateTripResponse expected = mock(CreateTripResponse.class);
    List<MultipartFile> images = List.of(mock(MultipartFile.class));
    when(userService.getAuthenticatedUser()).thenReturn(creator);
    when(request.getStatus()).thenReturn(TripStatus.COMPLETED);
    when(request.getTitle()).thenReturn("Holiday");
    when(request.getOrigin()).thenReturn("Warsaw");
    when(request.getDestination()).thenReturn("Krakow");
    when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(tripMapper.mapToCreateResponse(any(Trip.class))).thenReturn(expected);

    CreateTripResponse result = tripService.createTrip(request, images);

    ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
    verify(tripRepository).save(tripCaptor.capture());
    Trip savedTrip = tripCaptor.getValue();
    assertThat(savedTrip.getCreator()).isSameAs(creator);
    assertThat(savedTrip.getTitle()).isEqualTo("Holiday");
    assertThat(savedTrip.getOrigin()).isEqualTo("Warsaw");
    assertThat(savedTrip.getDestination()).isEqualTo("Krakow");
    assertThat(savedTrip.getStatus()).isEqualTo(TripStatus.COMPLETED);
    assertThat(result).isSameAs(expected);
    verify(usageService).incrementGoogleMapsUsage(creator);
    verify(tripImageService).addTripImages(savedTrip, images);
    verify(tripMapper).mapToCreateResponse(savedTrip);
  }

  @Test
  void createTrip_shouldRejectImagesForPlanningStatus() {
    User creator = user(1L);
    CreateTripRequest request = mock(CreateTripRequest.class);
    MultipartFile image = mock(MultipartFile.class);
    when(userService.getAuthenticatedUser()).thenReturn(creator);
    when(request.getStatus()).thenReturn(TripStatus.PLANNING);
    when(image.isEmpty()).thenReturn(false);

    assertThatThrownBy(() -> tripService.createTrip(request, List.of(image)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("PLANNING");

    verifyNoInteractions(usageService, tripRepository, tripImageService, tripMapper);
  }

  @Test
  void updateTrip_shouldRejectMissingTrip() {
    when(userService.getAuthenticatedUser()).thenReturn(user(1L));
    when(tripRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> tripService.updateTrip(99L, mock(UpdateTripRequest.class), List.of()))
            .isInstanceOf(ResourceNotFoundException.class);

    verify(tripRepository, never()).save(any(Trip.class));
    verifyNoInteractions(tripImageService, usageService);
  }

  @Test
  void updateTrip_shouldRejectOtherUsersTrip() {
    when(userService.getAuthenticatedUser()).thenReturn(user(2L));
    when(tripRepository.findById(10L)).thenReturn(Optional.of(tripOwnedBy(user(1L))));

    assertThatThrownBy(() -> tripService.updateTrip(10L, mock(UpdateTripRequest.class), List.of()))
            .isInstanceOf(AccessDeniedException.class);

    verify(tripRepository, never()).save(any(Trip.class));
    verifyNoInteractions(tripImageService, usageService);
  }

  @Test
  void updateTrip_shouldNotChargeUsageWhenRouteUnchanged() {
    User creator = user(1L);
    Trip trip = tripOwnedBy(creator);
    UpdateTripRequest request = mock(UpdateTripRequest.class);
    TripResponse expected = mock(TripResponse.class);
    when(userService.getAuthenticatedUser()).thenReturn(creator);
    when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));
    when(request.getOrigin()).thenReturn("Warsaw");
    when(request.getDestination()).thenReturn("Krakow");
    when(request.getStatus()).thenReturn(TripStatus.COMPLETED);
    when(tripRepository.save(trip)).thenReturn(trip);
    when(tripMapper.mapToResponse(trip)).thenReturn(expected);

    TripResponse result = tripService.updateTrip(10L, request, List.of());

    assertThat(result).isSameAs(expected);
    verify(usageService, never()).incrementGoogleMapsUsage(any(User.class));
    verify(tripImageService).removeTripImages(trip, List.of());
    verify(tripImageService).addTripImages(trip, List.of());
    verify(tripRepository).save(trip);
  }

  @Test
  void updateTrip_shouldChargeUsageWhenRouteChanges() {
    User creator = user(1L);
    Trip trip = tripOwnedBy(creator);
    UpdateTripRequest request = mock(UpdateTripRequest.class);
    when(userService.getAuthenticatedUser()).thenReturn(creator);
    when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));
    when(request.getOrigin()).thenReturn("Gdansk");
    when(request.getDestination()).thenReturn("Krakow");
    when(request.getStatus()).thenReturn(TripStatus.COMPLETED);
    when(tripRepository.save(trip)).thenReturn(trip);

    tripService.updateTrip(10L, request, List.of());

    assertThat(trip.getOrigin()).isEqualTo("Gdansk");
    verify(usageService).incrementGoogleMapsUsage(creator);
    verify(tripRepository).save(trip);
  }

  @Test
  void updateTrip_shouldRejectPlanningStatusWhenImagesRemain() {
    User creator = user(1L);
    Trip trip = tripOwnedBy(creator);
    TripImage image = new TripImage();
    image.setId(7L);
    trip.getTripImages().add(image);
    UpdateTripRequest request = mock(UpdateTripRequest.class);
    when(userService.getAuthenticatedUser()).thenReturn(creator);
    when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));
    when(request.getOrigin()).thenReturn("Warsaw");
    when(request.getDestination()).thenReturn("Krakow");
    when(request.getStatus()).thenReturn(TripStatus.PLANNING);

    assertThatThrownBy(() -> tripService.updateTrip(10L, request, List.of()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Remove all images");

    verify(tripRepository, never()).save(any(Trip.class));
    verifyNoInteractions(tripImageService, usageService);
  }

  @Test
  void deleteTrip_shouldRejectMissingTrip() {
    when(userService.getAuthenticatedUser()).thenReturn(user(1L));
    when(tripRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> tripService.deleteTrip(99L))
            .isInstanceOf(ResourceNotFoundException.class);

    verify(tripRepository, never()).delete(any(Trip.class));
    verifyNoInteractions(eventPublisher);
  }

  @Test
  void deleteTrip_shouldRejectOtherUsersTrip() {
    when(userService.getAuthenticatedUser()).thenReturn(user(2L));
    when(tripRepository.findById(10L)).thenReturn(Optional.of(tripOwnedBy(user(1L))));

    assertThatThrownBy(() -> tripService.deleteTrip(10L))
            .isInstanceOf(AccessDeniedException.class);

    verify(tripRepository, never()).delete(any(Trip.class));
    verifyNoInteractions(eventPublisher);
  }

  @Test
  void deleteTrip_shouldDeleteWithoutPublishingEventWhenNoImages() {
    User creator = user(1L);
    Trip trip = tripOwnedBy(creator);
    when(userService.getAuthenticatedUser()).thenReturn(creator);
    when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));

    tripService.deleteTrip(10L);

    verify(tripRepository).delete(trip);
    verifyNoInteractions(eventPublisher);
  }

  @Test
  void deleteTrip_shouldPublishImageIdsWhenImagesExist() {
    User creator = user(1L);
    Trip trip = tripOwnedBy(creator);
    TripImage first = new TripImage();
    first.setPublicId("cloud-1");
    TripImage second = new TripImage();
    second.setPublicId("cloud-2");
    trip.getTripImages().addAll(List.of(first, second));
    when(userService.getAuthenticatedUser()).thenReturn(creator);
    when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));

    tripService.deleteTrip(10L);

    verify(tripRepository).delete(trip);
    ArgumentCaptor<CloudinaryImageDeletionRequestedEvent> eventCaptor =
            ArgumentCaptor.forClass(CloudinaryImageDeletionRequestedEvent.class);
    verify(eventPublisher).publishEvent(eventCaptor.capture());
    assertThat(eventCaptor.getValue()).isNotNull();
  }
}
