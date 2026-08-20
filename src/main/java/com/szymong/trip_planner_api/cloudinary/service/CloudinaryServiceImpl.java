package com.szymong.trip_planner_api.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

  private final Cloudinary cloudinary;

  public CloudinaryServiceImpl(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }


  @Override
  public String uploadProfileImage(MultipartFile file) {
    return uploadImage(file,CloudinaryFolder.PROFILE_IMAGES);
  }

  @Override public String uploadTripImage(MultipartFile file){
    return uploadImage(file, CloudinaryFolder.TRIP_IMAGES);
  }

  private String uploadImage(MultipartFile file,CloudinaryFolder folder ) {
    try {
      Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("asset_folder", folder.getPath()));

      return uploadResult.get("public_id").toString();

    } catch (IOException e) {
      throw new RuntimeException("Failed to upload image to Cloudinary", e);
    }
  }

  @Getter
  public enum CloudinaryFolder {
    PROFILE_IMAGES("trip_planner_images/profile_images"),
    TRIP_IMAGES("trip_planner_images/trip_images");

    private final String path;

    CloudinaryFolder(String path) {
      this.path = path;
    }

  }
}
