package com.demo.user.service.dao;

import com.demo.user.service.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    UserSubscription findByUserIdAndTargetUserId(Long userId, Long targetUserId);
    List<UserSubscription> findByTargetUserId(Long targetUserId);
}

