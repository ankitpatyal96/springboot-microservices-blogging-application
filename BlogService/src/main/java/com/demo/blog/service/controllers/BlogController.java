package com.demo.blog.service.controllers;

import com.demo.blog.service.exceptions.TokenException;
import com.demo.blog.service.model.Blog;
import com.demo.blog.service.security.JwtTokenProvider;
import com.demo.blog.service.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        Blog createdBlog = blogService.createBlog(getCurrentUserId(authorizationHeader), blog);
        return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long blogId, @RequestBody Blog blog, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        Blog updatedBlog = blogService.updateBlog(getCurrentUserId(authorizationHeader), blogId, blog);
        return ResponseEntity.ok(updatedBlog);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long blogId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        blogService.deleteBlog(getCurrentUserId(authorizationHeader), blogId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long blogId) {
        Blog blog = blogService.getBlogById(blogId);
        return ResponseEntity.ok(blog);
    }

    @GetMapping
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Blog>> getBlogsByAuthorId(@PathVariable Long authorId) {
        List<Blog> blogs = blogService.getBlogsByAuthorId(authorId);
        return ResponseEntity.ok(blogs);
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
