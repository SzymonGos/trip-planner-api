package com.szymong.trip_planner_api.user.service;

import com.szymong.trip_planner_api.user.User;
import com.szymong.trip_planner_api.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  //todo: improve error handling
  public User getUserById(Long id) {
    Optional<User> result = userRepository.findById(id);

    if(result.isPresent()){
      return result.get();
    }
    else {
      throw new RuntimeException("id not found");
    }
  }

  public User getUserByClerkId(String clerkId){
    Optional<User> result = userRepository.findByClerkId(clerkId);

    if(result.isPresent()){
      return result.get();
    } else {
      throw new RuntimeException("clerkId not found" + clerkId);
    }
  }
}
