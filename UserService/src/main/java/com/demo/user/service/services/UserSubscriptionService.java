package com.demo.user.service.services;


import com.demo.user.service.model.UserSubscription;

import java.util.List;

public interface UserSubscriptionService {

    void subscribeUser(Long currentUserId, Long targetUserId);

    void unsubscribeUser(Long currentUserId, Long targetUserId);

    List<UserSubscription> getAllSubscribersForUser(Long userId);
}
