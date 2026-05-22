package com.szymong.trip_planner_api.user.controller;

import com.szymong.trip_planner_api.trip.Trip;
import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  public final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public User getUserById(@PathVariable Long id){
    return userService.getUserById(id);
  }

  @GetMapping("/me")
  public User getCurrentUser(){
    return userService.getCurrentUser();
  }

  @GetMapping("/me/trips")
  public List<Trip> getCurrentUserTrips(){
    return userService.getCurrentUserTrips();
  }

  @GetMapping("/{id}/trips")
  public List<Trip> getUserTrips(@PathVariable Long id){
    return userService.getUserTrips(id);
  }
}
