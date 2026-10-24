
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

    Trip trip = new Trip();
    trip.setCreator(user);
    trip.setTitle("Trip to Italy");
    trip.setOrigin("Warsaw");
    trip.setDestination("Rome");
    trip.setStatus(TripStatus.COMPLETED);
    trip.setDistanceMeters(300000L);
    trip.setEstimatedDurationSeconds(14400L);

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

}
