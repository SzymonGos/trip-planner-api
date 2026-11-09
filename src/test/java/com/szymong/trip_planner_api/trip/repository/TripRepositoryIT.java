
package com.szymong.trip_planner_api.trip.repository;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TripRepositoryIT {

  @Container
  @ServiceConnection
  static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

  @Autowired
  private TripRepository tripRepository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  void shouldConnectToPostgreSQL() {
    assertThat(tripRepository).isNotNull();
    assertThat(postgres.isRunning()).isTrue();
  }

  @Test
  void shouldDeleteTripWithAssociatedImages() {
    User user = new User();
    user.setClerkId("clerk_test_123");
    user.setEmail("test@example.com");
    user.setUsername("testuser");

    entityManager.persist(user);

    Trip trip = createTrip(user, "Trip to Italy", TripStatus.COMPLETED);

    TripImage image = new TripImage();
    image.setPublicId("cloudinary_test_123");
    image.setTrip(trip);

    trip.getTripImages().add(image);

    entityManager.persistAndFlush(trip);

    Long tripId = trip.getId();
    Long imageId = image.getId();

    entityManager.clear();

    Trip savedTrip = tripRepository.findById(tripId).orElseThrow();
    tripRepository.delete(savedTrip);
    tripRepository.flush();

    entityManager.clear();

    assertThat(tripRepository.findById(tripId)).isEmpty();

    assertThat(entityManager.find(TripImage.class, imageId)).isNull();
  }

  @Test
  void shouldFindOnlyCompletedTripsForSpecifiedUser() {
    User user1 = new User();
    user1.setClerkId("clerk_user_1");
    user1.setEmail("user1@example.com");
    user1.setUsername("user1");

    User user2 = new User();
    user2.setClerkId("clerk_user_2");
    user2.setEmail("user2@example.com");
    user2.setUsername("user2");

    entityManager.persist(user1);
    entityManager.persist(user2);

    Trip completedTrip = createTrip(user1, "Trip to Italy", TripStatus.COMPLETED);

    Trip planningTrip = createTrip(user1, "Trip to Germany", TripStatus.PLANNING);

    Trip otherUserTrip = createTrip(user2, "Trip to France", TripStatus.COMPLETED);

    entityManager.persist(completedTrip);
    entityManager.persist(planningTrip);
    entityManager.persist(otherUserTrip);

    entityManager.flush();
    entityManager.clear();

    List<Trip> result = tripRepository.findByCreatorIdAndStatus(user1.getId(), TripStatus.COMPLETED);

    assertThat(result).hasSize(1).extracting(Trip::getTitle).containsExactly("Trip to Italy");}

  private Trip createTrip(User creator, String title, TripStatus status
  ) {
    Trip trip = new Trip();

    trip.setCreator(creator);
    trip.setTitle(title);
    trip.setOrigin("Warsaw");
    trip.setDestination("Rome");
    trip.setStatus(status);
    trip.setDistanceMeters(300000L);
    trip.setEstimatedDurationSeconds(14400L);

    return trip;
  }
}
