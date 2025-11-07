package com.gym.dao;

import java.util.Optional;

import com.gym.model.User;

public interface IUserDAO {

  Optional<User> findByUsernameOrNameAndDtypeAndStatus(String input);

  Optional<User> findByUsernameOrNameAndRoleAndStatus(String input);
}
