package com.szymong.trip_planner_api.image.validation;

import com.szymong.trip_planner_api.image.config.ImageValidationProperties;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ImageFileValidator {

  private final ImageValidationProperties imageValidationProperties;
  private final Tika tika = new Tika();

  public ImageFileValidator(ImageValidationProperties imageValidationProperties) {
    this.imageValidationProperties = imageValidationProperties;
  }

  public void validateImage(MultipartFile image){
    if (image == null || image.isEmpty()) {
      return;
    }

    if(image.getSize() > imageValidationProperties.getMaxFileSize().toBytes() ){
      throw new RuntimeException("Image exceeds maximum size of " + imageValidationProperties.getMaxFileSize().toMegabytes() + "MB");
    }

    try (InputStream inputStream = image.getInputStream()) {
      String detectedContentType = tika.detect(
              inputStream,
              image.getOriginalFilename()
      );

      if (!imageValidationProperties.getAllowedContentTypes().contains(detectedContentType)) {
        throw new RuntimeException(
                "Unsupported image type: " + detectedContentType
        );
      }
    } catch (IOException exception) {
      throw new RuntimeException("Could not verify image file", exception);
    }

  }
}
