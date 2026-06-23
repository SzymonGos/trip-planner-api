package com.szymong.trip_planner_api.user;

import com.szymong.trip_planner_api.trip.Trip;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy =  GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name="clerk_id", nullable = false, unique = true)
  private String clerkId;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted;

  @Column(name = "google_maps_route_count", nullable = false)
  private Integer googleMapsRouteCount = 0;

  @Column(name = "google_maps_route_reset_date", nullable = false)
  private LocalDateTime googleMapsRouteResetDate = LocalDateTime.now().plusMonths(1);

  @Column(name = "ai_chat_usage_count", nullable = false)
  private Integer aiChatUsageCount = 0;

  @Column(name = "ai_chat_usage_reset_date", nullable = false)
  private LocalDateTime aiChatUsageResetDate = LocalDateTime.now().plusMonths(1);

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
  private List<Trip> trips = new ArrayList<>();

  public User(String clerkId, LocalDateTime createdAt, String email, String profileImageUrl, String username) {
    this.clerkId = clerkId;
    this.createdAt = createdAt;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
    this.username = username;
  }

}
