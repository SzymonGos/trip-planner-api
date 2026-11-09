package com.szymong.trip_planner_api.trip.service;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.trip.dto.CreateTripRequest;
import com.szymong.trip_planner_api.trip.dto.UpdateTripRequest;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.service.TripImageService;
import com.szymong.trip_planner_api.usage.service.UsageService;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.service.UserService;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TripServiceIT {

  @Autowired
  private TripService tripService;

  @Autowired
  private TripRepository tripRepository;

  @Autowired
  private EntityManager entityManager;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UsageService usageService;

  @MockitoBean
  private TripImageService tripImageService;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setClerkId("clerk_test_user");
    user.setEmail("test@example.com");
    user.setUsername("testuser");

    entityManager.persist(user);
    entityManager.flush();

    when(userService.getAuthenticatedUser())
            .thenReturn(user);
  }

  @Test
  void shouldCreateTripAndPersistInDatabase() {
    CreateTripRequest request = createTripRequest(TripStatus.COMPLETED);

    tripService.createTrip(request, List.of());

    entityManager.flush();
    entityManager.clear();

    List<Trip> trips = tripRepository.findAll();

    assertThat(trips).hasSize(1);

    Trip savedTrip = trips.get(0);

    assertThat(savedTrip.getTitle()).isEqualTo("Trip to Italy");

    assertThat(savedTrip.getDescription()).isEqualTo("Motorcycle trip");

    assertThat(savedTrip.getOrigin()).isEqualTo("Warsaw");

    assertThat(savedTrip.getDestination()).isEqualTo("Rome");

    assertThat(savedTrip.getStatus()).isEqualTo(TripStatus.COMPLETED);

    assertThat(savedTrip.getDistanceMeters()).isEqualTo(300000L);

    assertThat(savedTrip.getEstimatedDurationSeconds()).isEqualTo(14400L);

    assertThat(savedTrip.getCreator().getId()).isEqualTo(user.getId());

    verify(usageService).incrementGoogleMapsUsage(user);

    verify(tripImageService).addTripImages(any(Trip.class), eq(List.of()));
  }

  @Test
  void shouldUpdateTripAndPersistChanges() {
    Trip trip = createTrip(user, "Original title", TripStatus.COMPLETED);

    entityManager.persist(trip);
    entityManager.flush();
    Long tripId = trip.getId();

    entityManager.clear();

    UpdateTripRequest request = mock(UpdateTripRequest.class);

    when(request.getTitle()).thenReturn("Updated title");
    when(request.getDescription()).thenReturn("Updated description");
    when(request.getOrigin()).thenReturn("Warsaw");
    when(request.getDestination()).thenReturn("Rome");
    when(request.getStatus()).thenReturn(TripStatus.COMPLETED);
    when(request.getDistanceMeters()).thenReturn(500000L);
    when(request.getEstimatedDurationSeconds()).thenReturn(20000L);

    tripService.updateTrip(tripId, request, List.of());

    entityManager.flush();
    entityManager.clear();

    Trip updatedTrip = tripRepository.findById(tripId).orElseThrow();

    assertThat(updatedTrip.getTitle()).isEqualTo("Updated title");

    assertThat(updatedTrip.getDescription()).isEqualTo("Updated description");

    assertThat(updatedTrip.getDistanceMeters()).isEqualTo(500000L);

    assertThat(updatedTrip.getEstimatedDurationSeconds()).isEqualTo(20000L);

    assertThat(updatedTrip.getCreator().getId()).isEqualTo(user.getId());

    verify(usageService, never()).incrementGoogleMapsUsage(any(User.class));
  }

  @Test
  void shouldDeleteTripAndAssociatedImages() {
    Trip trip = createTrip(user, "Trip to Italy", TripStatus.COMPLETED);

    TripImage image = new TripImage();
    image.setPublicId("cloudinary_test_image");
    image.setTrip(trip);

    trip.getTripImages().add(image);

    entityManager.persist(trip);
    entityManager.flush();

    Long tripId = trip.getId();
    Long imageId = image.getId();

    entityManager.clear();

    tripService.deleteTrip(tripId);

    entityManager.flush();
    entityManager.clear();

    assertThat(tripRepository.findById(tripId)).isEmpty();

    assertThat(entityManager.find(TripImage.class, imageId)).isNull();
  }


  private Trip createTrip(User creator, String title, TripStatus status) {
    Trip trip = new Trip();

    trip.setCreator(creator);
    trip.setTitle(title);
    trip.setDescription("Motorcycle trip");
    trip.setOrigin("Warsaw");
    trip.setDestination("Rome");
    trip.setStatus(status);
    trip.setDistanceMeters(300000L);
    trip.setEstimatedDurationSeconds(14400L);

    return trip;
  }

  private CreateTripRequest createTripRequest(TripStatus status) {
    return new CreateTripRequest(
            "Trip to Italy",
            "Motorcycle trip",
            "Warsaw",
            "Rome",
            300000L,
            14400L,
            status,
            null,
            List.of()
    );
  }
}
