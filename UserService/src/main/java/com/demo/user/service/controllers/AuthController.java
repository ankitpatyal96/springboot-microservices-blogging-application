package com.demo.user.service.controllers;

import com.demo.user.service.payload.BlogUserDTO;
import com.demo.user.service.security.JwtResponse;
import com.demo.user.service.security.JwtTokenProvider;
import com.demo.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<BlogUserDTO> registerUser(@RequestBody BlogUserDTO userDTO) {
        BlogUserDTO registeredUser = userService.registerUser(userDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody BlogUserDTO userDTO) {
        JwtResponse jwtResponse = userService.authenticateUser(userDTO.getUsername(), userDTO.getPassword());
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/validate/{token}")
    public ResponseEntity<String> validate(@PathVariable String token) {
        boolean isValid = jwtTokenProvider.validateToken(token);
        String message = "Token is valid";
        if(!isValid) {
            message = "Token is invalid";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
