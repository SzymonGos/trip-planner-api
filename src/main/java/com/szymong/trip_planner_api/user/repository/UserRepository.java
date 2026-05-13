package com.szymong.trip_planner_api.user.repository;

import com.szymong.trip_planner_api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
