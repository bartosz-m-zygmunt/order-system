package com.example.notification_service.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class OrderEvent implements Serializable {
    private Long orderId;
    private String action;
    private Order order;
}
