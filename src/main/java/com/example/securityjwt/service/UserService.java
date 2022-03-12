package com.example.securityjwt.service;

import com.example.securityjwt.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByUsername(String username);
}
