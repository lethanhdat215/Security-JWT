package com.example.securityjwt.controller;

import com.example.securityjwt.dto.ResponseObject;
import com.example.securityjwt.entity.User;
import com.example.securityjwt.repository.UserRepository;
import com.example.securityjwt.security.CustomUserDetails;
import com.example.securityjwt.security.jwt.JwtTokenProvider;
import com.example.securityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createAccount(@RequestBody User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new
                    ResponseObject("False", "Username already", "",""));
        } else {
            String password = user.getPassword().trim();
            String passwordEncoder = bCryptPasswordEncoder.encode(password).trim();
            user.setPassword(passwordEncoder);
            return ResponseEntity.status(HttpStatus.OK).body(new
                    ResponseObject("OK", "Successfully","", userRepository.save(user)));
        }
    }

    @PostMapping(value = "/login")
    ResponseEntity<ResponseObject> loginAccount(@RequestBody User user) {
        Optional<User> userOptional = userService.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            if (bCryptPasswordEncoder.matches(user.getPassword(), userOptional.get().getPassword())) {
                String token = jwtProvider.generateToken(userOptional);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "login successfully",  token, userOptional));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("False", "Password wrong", "", ""));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("False", "wrong username", "", ""));
        }
    }


}


