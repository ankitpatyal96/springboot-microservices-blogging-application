package com.demo.user.service.dao;

import com.demo.user.service.model.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<BlogUser, Long> {
    BlogUser findByUsername(String username);

    boolean existsByEmail(String email);

}

