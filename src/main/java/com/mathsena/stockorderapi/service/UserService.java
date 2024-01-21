package com.mathsena.stockorderapi.service;

import com.mathsena.stockorderapi.model.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;


public interface UserService {

  List<User> getAllUsers();

  User getUserById(Long id) throws EntityNotFoundException;

  User createUser(User newUserDTO);

  User updateUser(Long id, User updatedUserDTO) throws EntityNotFoundException;

  boolean deleteUser(Long id);
}
