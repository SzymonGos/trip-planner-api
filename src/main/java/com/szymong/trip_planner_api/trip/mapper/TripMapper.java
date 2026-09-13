package com.szymong.trip_planner_api.trip.mapper;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.trip.dto.CreateTripResponse;
import com.szymong.trip_planner_api.trip.dto.TripCreatorResponse;
import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.tripImage.mapper.TripImageMapper;
import com.szymong.trip_planner_api.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TripImageMapper.class)
public interface TripMapper {

  TripResponse mapToResponse(Trip trip);

  CreateTripResponse mapToCreateResponse(Trip trip);

  TripCreatorResponse mapCreatorToResponse(User creator);
}
