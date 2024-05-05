package com.demo.blog.service.services;

import com.demo.blog.service.model.Blog;

import java.util.List;

public interface BlogService {

    Blog createBlog(Long authorId, Blog blog);

    Blog updateBlog(Long authorId, Long blogId, Blog blog);

    void deleteBlog(Long authorId, Long blogId);

    List<Blog> getAllBlogs();

    List<Blog> getBlogsByAuthorId(Long authorId);

    Blog getBlogById(Long blogId);
}
