package org.example.dao;

import org.example.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {

    Optional<User> findByUsername(String username);
    Collection<User> findAll();
    void update(User user);
}
