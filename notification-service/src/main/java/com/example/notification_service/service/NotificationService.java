package com.example.notification_service.service;

import com.example.notification_service.enumeration.NotificationType;
import com.example.notification_service.model.NotificationRequest;
import com.example.notification_service.model.Order;
import com.example.notification_service.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class NotificationService {

    private static final String ORDER_QUEUE = "orderQueue";
    private static final int THREAD_SLEEP_DURATION_FOR_QUEUE_MESSAGES_LOOKUP = 0;

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final Map<NotificationType, NotificationStrategy> notificationStrategies = new HashMap<>();

    @Autowired
    public NotificationService(NotificationStrategyEmail emailStrategy,
                               NotificationStrategySms smsStrategy,
                               NotificationStrategyPush pushStrategy) {
        notificationStrategies.put(NotificationType.EMAIL, emailStrategy);
        notificationStrategies.put(NotificationType.SMS, smsStrategy);
        notificationStrategies.put(NotificationType.PUSH, pushStrategy);
    }

    @RabbitListener(queues = ORDER_QUEUE)
    public void handleOrderEvent(OrderEvent orderEvent) {
        try {
            Thread.sleep(THREAD_SLEEP_DURATION_FOR_QUEUE_MESSAGES_LOOKUP);

            logOrderEventDetails(orderEvent);
            processOrderEvent(orderEvent.getOrder());
            logger.info("📥 OrderEvent has been received: {}", orderEvent);
        } catch (Exception e) {
            logUnexpectedError(e);
        }
    }

    private void logOrderEventDetails(OrderEvent orderEvent) {
        switch (orderEvent.getAction()) {
            case "CREATE":
                logger.info("🔔 Order was created: {}", orderEvent.getOrder());
                break;
            case "GET":
                logger.info("🔍 Order was requested: {}", orderEvent.getOrder());
                break;
            case "DELETE":
                logger.info("🗑️ Order was deleted: {}", orderEvent.getOrder());
                break;
            default:
                logger.error("❌ Unknown event action: {}", orderEvent.getAction());
        }
    }

    private void logUnexpectedError(Exception e) {
        logger.error("❌ An unexpected error occurred: {}", e.getMessage(), e);
    }

    private void processOrderEvent(Order order) {
        NotificationRequest notificationRequest = createNotificationRequest(order);
        sendNotification(notificationRequest, order.getNotificationType());
    }

    private NotificationRequest createNotificationRequest(Order order) {
        String message = String.format("✅ Order %s for customer %s is in status %s",
                order.getOrderNumber(), order.getCustomerName(), order.getStatusType());
        return new NotificationRequest(order.getCustomerName(), message);
    }

    private void sendNotification(NotificationRequest notificationRequest, NotificationType notificationType) {
        NotificationStrategy notificationStrategy = notificationStrategies.get(notificationType);
        if (notificationStrategy != null) {
            notificationStrategy.sendNotification(notificationRequest);
        } else {
            logger.warn("❗ Unknown notification type: {}", notificationType);
        }
    }
}