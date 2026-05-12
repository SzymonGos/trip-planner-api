package com.szymong.trip_planner_api.trip;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "origin")
  private String origin;

  @Column(name = "destination")
  private String destination;

 // todo: create status enum
  @Column(name = "status")
  private TripStatus status;

  @Column(name = "estimated_duration")
  private String estimatedDuration;

  @Column(name = "creator_id")
  private Long creatorId;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Trip(Long creatorId, String description, String destination, String estimatedDuration, String origin, TripStatus status, String title) {
    this.creatorId = creatorId;
    this.description = description;
    this.destination = destination;
    this.estimatedDuration = estimatedDuration;
    this.origin = origin;
    this.status = status;
    this.title = title;
  }
}
