package com.demo.user.service.controllers;

import com.demo.user.service.exceptions.TokenException;
import com.demo.user.service.model.UserSubscription;
import com.demo.user.service.security.JwtTokenProvider;
import com.demo.user.service.services.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription")
public class UserSubscriptionController {

    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/subscribe/{targetUserId}")
    public ResponseEntity<String> subscribeUser(@PathVariable Long targetUserId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        userSubscriptionService.subscribeUser(getCurrentUserId(authorizationHeader), targetUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Subscribed successfully");
    }

    @PostMapping("/unsubscribe/{targetUserId}")
    public ResponseEntity<String> unsubscribeUser(@PathVariable Long targetUserId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        userSubscriptionService.unsubscribeUser(getCurrentUserId(authorizationHeader), targetUserId);
        return ResponseEntity.status(HttpStatus.OK).body("Unsubscribed successfully");
    }

    @GetMapping("/subscribers/{userid}")
    public ResponseEntity<List<UserSubscription>> getSubscribersList(@PathVariable Long userid) {
        List<UserSubscription> subscribers = userSubscriptionService.getAllSubscribersForUser(userid);
        return new ResponseEntity<>(subscribers, HttpStatus.OK);
    }

    private Long getCurrentUserId(String authHeader) {
        String token = "";
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
            boolean isValidToken = jwtTokenProvider.validateToken(token);
            if (isValidToken) {
                return jwtTokenProvider.getUserIdFromJWT(token);
            } else {
                throw new TokenException("Invalid Token");
            }
        } catch (Exception ex) {
            throw new TokenException("Invalid Token");
        }
    }
}
