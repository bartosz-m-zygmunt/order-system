package com.example.notification_service.service;

import com.example.notification_service.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationStrategyEmail implements NotificationStrategy {

    private final Logger logger = LoggerFactory.getLogger(NotificationStrategyEmail.class);

    @Override
    public void sendNotification(NotificationRequest notificationRequest) {
        logger.info("EMAIL: The user {} received the following message {}",
                notificationRequest.getRecipient(), notificationRequest.getMessage());

        //TODO Sending e-mail logic (eg. JavaMailSender)
    }
}
