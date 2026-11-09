
package com.szymong.trip_planner_api.trip.controller;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.TripStatus;
import com.szymong.trip_planner_api.trip.repository.TripRepository;
import com.szymong.trip_planner_api.tripImage.service.TripImageService;
import com.szymong.trip_planner_api.usage.service.UsageService;
import com.szymong.trip_planner_api.user.User;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TripControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TripRepository tripRepository;

  @Autowired
  private EntityManager entityManager;

  @MockitoBean
  private JwtDecoder jwtDecoder;

  @MockitoBean
  private UsageService usageService;

  @MockitoBean
  private TripImageService tripImageService;

  @Test
  void shouldReturnTripWhenExists() throws Exception {
    User user = createUser();
    Trip trip = createTrip(user);

    entityManager.persist(trip);
    entityManager.flush();

    Long tripId = trip.getId();

    entityManager.clear();

    mockMvc.perform(get("/api/trips/{id}", tripId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(tripId))
            .andExpect(jsonPath("$.title").value("Trip to Italy"))
            .andExpect(jsonPath("$.origin").value("Warsaw"))
            .andExpect(jsonPath("$.destination").value("Rome"))
            .andExpect(jsonPath("$.status").value("COMPLETED"))
            .andExpect(jsonPath("$.distanceMeters").value(300000));

  }

  @Test
  void shouldReturn404WhenTripDoesNotExist() throws Exception {
    mockMvc.perform(get("/api/trips/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
  }

  @Test
  void shouldRejectUnauthenticatedCreateTrip() throws Exception {
    MockMultipartFile request = new MockMultipartFile(
            "request",
            "",
            "application/json",
            """
            {
                "title": "Trip to Italy",
                "origin": "Warsaw",
                "destination": "Rome",
                "distanceMeters": 300000,
                "estimatedDurationSeconds": 14400,
                "status": "COMPLETED"
            }
            """.getBytes(java.nio.charset.StandardCharsets.UTF_8)
    );

    mockMvc.perform(
                    multipart("/api/trips")
                            .file(request)
            )
            .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldCreateTripWithAuthenticatedUser() throws Exception {
    User user = createUser();

    Jwt jwt = Jwt.withTokenValue("test-token")
                      .header("alg", "RS256")
                      .subject(user.getClerkId())
                      .build();

    when(jwtDecoder.decode("test-token"))
            .thenReturn(jwt);

    MockMultipartFile request = new MockMultipartFile(
            "request",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            """
            {
                "title": "Authenticated trip",
                "description": "Motorcycle journey",
                "origin": "Warsaw",
                "destination": "Rome",
                "distanceMeters": 300000,
                "estimatedDurationSeconds": 14400,
                "status": "COMPLETED"
            }
            """.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
                    multipart("/api/trips")
                            .file(request)
                            .header(
                                    HttpHeaders.AUTHORIZATION,
                                    "Bearer test-token"
                            )
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title")
                               .value("Authenticated trip"));

    entityManager.flush();
    entityManager.clear();

    List<Trip> trips = tripRepository.findAll();

    assertThat(trips).hasSize(1);

    Trip savedTrip = trips.get(0);

    assertThat(savedTrip.getTitle())
            .isEqualTo("Authenticated trip");

    assertThat(savedTrip.getCreator().getId())
            .isEqualTo(user.getId());

    assertThat(savedTrip.getStatus())
            .isEqualTo(TripStatus.COMPLETED);

    assertThat(savedTrip.getDistanceMeters())
            .isEqualTo(300000L);

    verify(usageService)
            .incrementGoogleMapsUsage(any(User.class));

    verify(tripImageService)
            .addTripImages(any(Trip.class), isNull());
  }

  private User createUser() {
    User user = new User();

    user.setClerkId("clerk_controller_test");
    user.setEmail("controller@example.com");
    user.setUsername("controlleruser");

    entityManager.persist(user);
    entityManager.flush();

    return user;
  }

  private Trip createTrip(User user) {
    Trip trip = new Trip();

    trip.setCreator(user);
    trip.setTitle("Trip to Italy");
    trip.setDescription("Motorcycle trip");
    trip.setOrigin("Warsaw");
    trip.setDestination("Rome");
    trip.setStatus(TripStatus.COMPLETED);
    trip.setDistanceMeters(300000L);
    trip.setEstimatedDurationSeconds(14400L);

    return trip;
  }
}
