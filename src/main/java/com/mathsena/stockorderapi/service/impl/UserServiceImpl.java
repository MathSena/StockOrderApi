package com.mathsena.stockorderapi.service.impl;

import com.mathsena.stockorderapi.model.User;
import com.mathsena.stockorderapi.repository.UserRepository;
import com.mathsena.stockorderapi.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> getAllUsers() {
    return new ArrayList<>(userRepository.findAll());
  }

  @Override
  public User getUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    return user.orElse(null);
  }

  @Override
  public User createUser(User newUser) {
    if (newUser == null) {
      throw new IllegalArgumentException("User object cannot be null");
    }
    if (newUser.getEmail() == null || newUser.getName() == null) {
      throw new IllegalArgumentException("Name and email must not be null");
    }
    return userRepository.save(newUser);
  }

  @Override
  @Transactional
  public User updateUser(Long id, User updatedUserDTO) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setName(updatedUserDTO.getName());
      user.setEmail(updatedUserDTO.getEmail());
      return user;
    }
    return null;
  }

  @Override
  public boolean deleteUser(Long id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
