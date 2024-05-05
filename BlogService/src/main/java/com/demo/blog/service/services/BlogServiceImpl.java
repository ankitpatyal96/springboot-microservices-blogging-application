package com.demo.blog.service.services;

import com.demo.blog.service.exceptions.BlogNotFoundException;
import com.demo.blog.service.exceptions.ForbiddenModificationException;
import com.demo.blog.service.model.Blog;
import com.demo.blog.service.dao.BlogRepository;
import com.demo.blog.service.model.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private KafkaNotificationService kafkaNotificationService;

    @Override
    public Blog createBlog(Long authorId, Blog blogDTO) {

        Blog blog = new Blog();
        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        blog.setAuthorId(authorId);
        Blog entity = blogRepository.save(blog);
        NotificationMessage msg = NotificationMessage.builder().authorId(authorId).title(blogDTO.getTitle()).build();
        kafkaNotificationService.notifyBlogCreation(msg);
        return entity;
    }

    @Override
    public Blog updateBlog(Long authorId, Long blogId, Blog blog) {
        // Validate if the blog belongs to the authenticated user
        Blog existingBlog = blogRepository.findById(blogId).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
        if (!Objects.equals(authorId, existingBlog.getAuthorId())) {
            throw new ForbiddenModificationException("Author: " + authorId +
                    " does not have sufficient privileges to modify the blog " + blogId);
        }
        // Update blog details
        existingBlog.setTitle(blog.getTitle());
        existingBlog.setContent(blog.getContent());
        existingBlog.setCreatedAt(LocalDateTime.now());

        return blogRepository.save(existingBlog);
    }

    @Override
    public void deleteBlog(Long authorId, Long blogId) {
        // Validate if the blog belongs to the authenticated user
        Blog existingBlog = blogRepository.findById(blogId).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
        if (!Objects.equals(authorId, existingBlog.getAuthorId())) {
            throw new ForbiddenModificationException("Author: " + authorId +
                    " does not have sufficient privileges to delete the blog " + blogId);
        }
        blogRepository.deleteById(blogId);
    }

    @Override
    public Blog getBlogById(Long blogId) {
        return blogRepository.findById(blogId).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
    }

    @Override
    public List<Blog> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return new ArrayList<>(blogs);
    }

    @Override
    public List<Blog> getBlogsByAuthorId(Long authorId) {
        List<Blog> blogs = blogRepository.findByAuthorId(authorId);
        return new ArrayList<>(blogs);
    }
}
