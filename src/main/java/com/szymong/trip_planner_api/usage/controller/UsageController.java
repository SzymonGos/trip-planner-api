package com.szymong.trip_planner_api.usage.controller;

import com.szymong.trip_planner_api.usage.dto.UsageResponse;
import com.szymong.trip_planner_api.usage.service.UsageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsageController {

  public final UsageService usageService;

  public UsageController(UsageService usageService) {
    this.usageService = usageService;
  }

  @GetMapping("/me/usage")
  public UsageResponse getUserUsage(){
    return usageService.getUserUsage();
  }
}
