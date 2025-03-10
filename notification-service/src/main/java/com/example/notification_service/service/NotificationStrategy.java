package com.example.notification_service.service;

import com.example.notification_service.model.NotificationRequest;

public interface NotificationStrategy {
    void sendNotification(NotificationRequest notificationRequest);
}