package com.szymong.trip_planner_api.user.repository;

import com.szymong.trip_planner_api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByClerkId(String clerkId);
}
