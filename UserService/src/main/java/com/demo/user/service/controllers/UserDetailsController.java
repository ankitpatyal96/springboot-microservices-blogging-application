package com.demo.user.service.controllers;

import com.demo.user.service.payload.BlogUserDTO;
import com.demo.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserDetailsController {

    private final UserService userService;

    @Autowired
    public UserDetailsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BlogUserDTO> getBlogUserById(@PathVariable Long userId) {
        BlogUserDTO userDTO = userService.getBlogUserById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BlogUserDTO>> getAllBlogUsers() {
        List<BlogUserDTO> userDTOList = userService.getAllBlogUsers();
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }
}
