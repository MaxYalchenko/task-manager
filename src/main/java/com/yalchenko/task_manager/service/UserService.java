package com.yalchenko.task_manager.service;

import com.yalchenko.task_manager.entity.User;


public interface UserService {

    User register(User user);

    User findByEmail(String email);

    boolean hasRole(User user, String role);
    boolean existsByEmail(String email);
}
