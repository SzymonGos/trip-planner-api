package com.szymong.trip_planner_api.tripImage;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "trip_images")
public class TripImage {

  @Id
  @GeneratedValue(strategy =  GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "trip_id", nullable = false)
  private Long tripId;

  @Column(name = "url", nullable = false)
  private String url;

  @Column(name = "public_id", nullable = false)
  private String publicId;

  public TripImage(String publicId, Long tripId, String url) {
    this.publicId = publicId;
    this.tripId = tripId;
    this.url = url;
  }
}
