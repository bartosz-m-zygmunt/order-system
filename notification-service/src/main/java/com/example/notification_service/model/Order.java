package com.example.notification_service.model;

import com.example.notification_service.enumeration.NotificationType;
import com.example.notification_service.enumeration.StatusType;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Order implements Serializable {
    private Long id;
    private Long orderNumber;
    private String customerName;
    private StatusType statusType;
    private BigDecimal price;
    private NotificationType notificationType;
}