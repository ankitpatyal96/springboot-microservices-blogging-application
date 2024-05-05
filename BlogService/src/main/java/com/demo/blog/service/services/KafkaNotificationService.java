package com.demo.blog.service.services;

import com.demo.blog.service.constants.AppConstants;
import com.demo.blog.service.model.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaNotificationService {

    @Autowired
    private KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public void notifyBlogCreation(NotificationMessage message) {
        this.kafkaTemplate.send(AppConstants.POST_CREATION_TOPIC_NAME, message);
    }

}