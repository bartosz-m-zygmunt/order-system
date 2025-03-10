package com.example.order_service.model;

import com.example.order_service.enumeration.NotificationType;
import com.example.order_service.enumeration.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false)
    private Long orderNumber;

    @Column(name = "customer_name", length = 100, nullable = false)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private StatusType statusType;

    @Column(name = "price", precision = 19, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 50, nullable = false)
    private NotificationType notificationType;

    @Version
    private Long version;
}

