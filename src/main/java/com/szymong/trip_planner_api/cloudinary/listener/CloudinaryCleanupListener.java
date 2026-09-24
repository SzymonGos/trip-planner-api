package com.szymong.trip_planner_api.cloudinary.listener;

import com.szymong.trip_planner_api.cloudinary.event.CloudinaryImageDeletionRequestedEvent;
import com.szymong.trip_planner_api.cloudinary.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudinaryCleanupListener {
  private final CloudinaryService cloudinaryService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(CloudinaryImageDeletionRequestedEvent event){
    for(String publicId: event.publicIds()){
      try{
        cloudinaryService.deleteImage(publicId);
      }catch (RuntimeException exception){
        log.error( "Failed to delete Cloudinary image with publicId: {}",publicId, exception);
      }
    }
  }
}
