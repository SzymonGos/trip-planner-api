package com.szymong.trip_planner_api.trip;

import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "trips")
public class Trip {

  @Id
  @GeneratedValue(strategy =  GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "origin", nullable = false)
  private String origin;

  @Column(name = "destination", nullable = false)
  private String destination;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private TripStatus status;

  @NotNull
  @Positive
  @Column(name = "estimated_duration_seconds", nullable = false)
  private Long estimatedDurationSeconds;

  @NotNull
  @Positive
  @Column(name = "distance_meters", nullable = false)
  private Long distanceMeters;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id", nullable = false)
  private User creator;

  @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TripImage> tripImages = new ArrayList<>();

  public Trip(String description, String destination, Long estimatedDurationSeconds, Long distanceMeters, String origin, TripStatus status, String title) {
    this.description = description;
    this.destination = destination;
    this.estimatedDurationSeconds = estimatedDurationSeconds;
    this.distanceMeters = distanceMeters;
    this.origin = origin;
    this.status = status;
    this.title = title;
  }
}
