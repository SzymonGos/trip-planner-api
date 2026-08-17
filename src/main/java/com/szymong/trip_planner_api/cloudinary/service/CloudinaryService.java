package com.szymong.trip_planner_api.cloudinary.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
  String uploadProfileImage(MultipartFile file);

  String uploadTripImage(MultipartFile file);
}
