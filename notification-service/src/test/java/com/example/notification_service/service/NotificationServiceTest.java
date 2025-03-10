package com.example.notification_service.service;

import com.example.notification_service.enumeration.NotificationType;
import com.example.notification_service.enumeration.StatusType;
import com.example.notification_service.model.NotificationRequest;
import com.example.notification_service.model.Order;
import com.example.notification_service.model.OrderEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationStrategyEmail emailStrategy;

    @Mock
    private NotificationStrategySms smsStrategy;

    @Mock
    private NotificationStrategyPush pushStrategy;

    private Order order;
    private OrderEvent orderEvent;

    @BeforeEach
    public void setUp() {
        order = Order.builder()
                .id(1L)
                .orderNumber(123L)
                .customerName("Test Customer")
                .statusType(StatusType.CREATED)
                .notificationType(NotificationType.EMAIL)
                .build();

        orderEvent = new OrderEvent(123L, "CREATE", order);
    }

    @Test
    public void shouldSendEmailNotification() {
        // Given
        NotificationRequest expectedRequest = new NotificationRequest("Test Customer",
                "✅ Order 123 for customer Test Customer is in status CREATED");

        // When
        notificationService.handleOrderEvent(orderEvent);

        // Then
        verify(emailStrategy, times(1)).sendNotification(expectedRequest);
        verify(smsStrategy, never()).sendNotification(any());
        verify(pushStrategy, never()).sendNotification(any());
    }

    @Test
    public void shouldSendSmsNotification() {
        // Given
        order.setNotificationType(NotificationType.SMS);
        OrderEvent smsOrderEvent = new OrderEvent(123L, "CREATE", order);
        NotificationRequest expectedRequest = new NotificationRequest("Test Customer",
                "✅ Order 123 for customer Test Customer is in status CREATED");

        // When
        notificationService.handleOrderEvent(smsOrderEvent);

        // Then
        verify(smsStrategy, times(1)).sendNotification(expectedRequest);
        verify(emailStrategy, never()).sendNotification(any());
        verify(pushStrategy, never()).sendNotification(any());
    }

    @Test
    public void shouldSendPushNotification() {
        // Given
        order.setNotificationType(NotificationType.PUSH);
        OrderEvent pushOrderEvent = new OrderEvent(123L, "CREATE", order);
        NotificationRequest expectedRequest = new NotificationRequest("Test Customer",
                "✅ Order 123 for customer Test Customer is in status CREATED");

        // When
        notificationService.handleOrderEvent(pushOrderEvent);

        // Then
        verify(pushStrategy, times(1)).sendNotification(expectedRequest);
        verify(emailStrategy, never()).sendNotification(any());
        verify(smsStrategy, never()).sendNotification(any());
    }

    @Test
    public void shouldHandleNullOrderEvent() {
        // When
        notificationService.handleOrderEvent(null);

        // Then
        verify(emailStrategy, never()).sendNotification(any());
        verify(smsStrategy, never()).sendNotification(any());
        verify(pushStrategy, never()).sendNotification(any());
    }

    @Test
    public void shouldHandleUnknownNotificationType() {
        // Given: Set notification type to null as an unknown type
        order.setNotificationType(null);
        OrderEvent unknownOrderEvent = new OrderEvent(123L, "CREATE", order);

        // When
        notificationService.handleOrderEvent(unknownOrderEvent);

        // Then
        verify(emailStrategy, never()).sendNotification(any());
        verify(smsStrategy, never()).sendNotification(any());
        verify(pushStrategy, never()).sendNotification(any());
    }
}