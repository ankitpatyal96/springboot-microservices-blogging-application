package com.demo.user.service.services;

import com.demo.user.service.dao.UserRepository;
import com.demo.user.service.dao.UserSubscriptionRepository;
import com.demo.user.service.exceptions.UserNotFoundException;
import com.demo.user.service.model.UserSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void subscribeUser(Long currentUserId, Long targetUserId) {
        if (!userRepository.existsById(targetUserId)) {
            throw new UserNotFoundException("Target user with ID " + targetUserId + " does not exist");
        }
        UserSubscription userSubscription =
                userSubscriptionRepository.findByUserIdAndTargetUserId(currentUserId, targetUserId);
        // subscribe if not already subscribed
        if (userSubscription == null) {
            UserSubscription newSubscription = UserSubscription.builder().userId(currentUserId).targetUserId(targetUserId).build();
            userSubscriptionRepository.save(newSubscription);
        }
    }

    @Override
    public void unsubscribeUser(Long currentUserId, Long targetUserId) {
        if (!userRepository.existsById(targetUserId)) {
            throw new UserNotFoundException("Target user with ID " + targetUserId + " does not exist");
        }
        UserSubscription userSubscription =
                userSubscriptionRepository.findByUserIdAndTargetUserId(currentUserId, targetUserId);
        // unsubscribe only if already subscribed
        if (userSubscription != null) {
            userSubscriptionRepository.deleteById(userSubscription.getId());
        }
    }

    @Override
    public List<UserSubscription> getAllSubscribersForUser(Long userId) {
        return userSubscriptionRepository.findByTargetUserId(userId);
    }
}
