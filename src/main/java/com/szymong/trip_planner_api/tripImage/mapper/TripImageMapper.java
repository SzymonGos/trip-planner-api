package com.szymong.trip_planner_api.tripImage.mapper;

import com.szymong.trip_planner_api.tripImage.TripImage;
import com.szymong.trip_planner_api.tripImage.dto.TripImageResponse;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface TripImageMapper {
  TripImageResponse mapToResponse(TripImage tripImage);
}
