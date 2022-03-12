package com.example.securityjwt.security;

import com.example.securityjwt.entity.User;
import com.example.securityjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Kiểm tra xem user có tồn tại trong database không?
        Optional<User> user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user.get());
    }


    public UserDetails loadUserById(Long userId) {
        // Kiểm tra xem user có tồn tại trong database không?
        Optional<User> user = userRepository.findById(userId);
        if (user == null) {
            throw new NullPointerException();
        }
        return new CustomUserDetails(user.get());
    }
}