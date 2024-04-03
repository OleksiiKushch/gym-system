package org.example.dao;

import org.example.entity.User;

import java.util.Collection;

public interface UserDao {

    Collection<User> findAll();
}
