package com.example.notification_service.service;

import com.example.notification_service.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationStrategySms implements NotificationStrategy {

    private final Logger logger = LoggerFactory.getLogger(NotificationStrategySms.class);
    @Override
    public void sendNotification(NotificationRequest notificationRequest) {
        logger.info("SMS: The user {} received the following message {}",
                notificationRequest.getRecipient(), notificationRequest.getMessage());

        //TODO Sending SMS logic (eg. Twilio)
    }
}
