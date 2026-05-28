package com.szymong.trip_planner_api.user.mapper;

import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponse mapToResponse(User user);
}
