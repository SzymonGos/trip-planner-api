package com.szymong.trip_planner_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TripPlannerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripPlannerApiApplication.class, args);
	}

}
