package com.szymong.trip_planner_api.trip.mapper;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.tripImage.mapper.TripImageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TripImageMapper.class)
public interface TripMapper {

  @Mapping(source = "creator.id", target = "creatorId")
  TripResponse mapToResponse(Trip trip);
}
