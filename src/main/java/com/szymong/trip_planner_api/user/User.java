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

  @Column(name="clerk_id", nullable = false)
  private String clerkId;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

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
