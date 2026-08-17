package com.szymong.trip_planner_api.user.controller;

import com.szymong.trip_planner_api.trip.dto.TripResponse;
import com.szymong.trip_planner_api.user.dto.*;
import com.szymong.trip_planner_api.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  public final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public UserResponse getUserById(@PathVariable Long id){
    return userService.getUserById(id);
  }

  @GetMapping("/me")
  public CurrentUserResponse getCurrentUser(){
    return userService.getCurrentUser();
  }

  @GetMapping("/username/{username}")
  public UserResponse getUserByUsername(@PathVariable String username){
    return userService.getUserByUsername(username);
  }

  @GetMapping("/me/trips")
  public List<TripResponse> getCurrentUserTrips(){
    return userService.getCurrentUserTrips();
  }

  @GetMapping("/{id}/trips")
  public List<TripResponse> getUserTrips(@PathVariable Long id){
    return userService.getUserTrips(id);
  }

  @PostMapping
  public CreateUserResponse createUser(@RequestBody CreateUserRequest request) { return userService.createUser(request); }

  @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UpdateCurrentUserResponse updateUser(@RequestPart("request") UpdateCurrentUserRequest request, @RequestPart(value = "profileImage", required = false) MultipartFile profileImage){
    return userService.updateUser(request, profileImage);
  }
}
