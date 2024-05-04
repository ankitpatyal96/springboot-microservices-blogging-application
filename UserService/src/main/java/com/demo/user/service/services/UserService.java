package com.demo.user.service.services;

import com.demo.user.service.payload.BlogUserDTO;
import com.demo.user.service.security.JwtResponse;

import java.util.List;

public interface UserService {
    BlogUserDTO registerUser(BlogUserDTO user);

    JwtResponse authenticateUser(String username, String password);

    BlogUserDTO getBlogUserById(Long userId);

    List<BlogUserDTO> getAllBlogUsers();

    Long getDetailsFromToken(String token);
}
