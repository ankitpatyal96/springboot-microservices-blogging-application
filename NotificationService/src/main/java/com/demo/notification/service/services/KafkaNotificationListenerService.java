package com.demo.notification.service.services;

import com.demo.notification.service.constants.AppConstants;
import com.demo.notification.service.model.NotificationMessage;
import com.demo.notification.service.model.UserSubscription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KafkaNotificationListenerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @KafkaListener(topics = AppConstants.POST_CREATION_TOPIC_NAME, groupId = AppConstants.GROUP_ID)
    public void notifyBlogCreation(String messageJson) {
        // Deserialize JSON string into NotificationMessage object
        NotificationMessage message = null;
        try {
            message = objectMapper.readValue(messageJson, NotificationMessage.class);
            System.out.println("<<<<<<<<<<<<<<<<< NOTIFICATION <<<<<<<<<<<<<<<<<<<");
            String subscribersUrl = "http://USER-SERVICE/api/subscription/subscribers/" + message.getAuthorId();
            UserSubscription[] subscribers = restTemplate.getForObject(subscribersUrl, UserSubscription[].class);
            if(subscribers == null) {
                throw new RuntimeException("failed to get list of subscribers for author: "+ message.getAuthorId());
            }
            for (UserSubscription subscriber : subscribers) {
                System.out.println("Sending notification to Subscriber ID: " + subscriber.getUserId());
                System.out.println("Author: " + message.getAuthorId()+ " has uploaded a new blog: "+message.getTitle());
            }
        } catch (JsonProcessingException e) {
            System.out.println("Failed to notify the users due to below exception:");
            System.out.println(e.getMessage());
        }
    }

}