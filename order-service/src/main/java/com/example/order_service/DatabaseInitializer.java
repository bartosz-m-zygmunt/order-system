package com.example.order_service;

import com.example.order_service.enumeration.NotificationType;
import com.example.order_service.enumeration.StatusType;
import com.example.order_service.model.OrderEntity;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) {
        try {
            OrderEntity order1 = OrderEntity.builder()
                    .orderNumber(1001L)
                    .customerName("John Doe")
                    .statusType(StatusType.SHIPPED)
                    .price(new BigDecimal("199.99"))
                    .notificationType(NotificationType.EMAIL)
                    .build();

            OrderEntity order2 = OrderEntity.builder()
                    .orderNumber(1002L)
                    .customerName("Jane Smith")
                    .statusType(StatusType.CREATED)
                    .price(new BigDecimal("249.99"))
                    .notificationType(NotificationType.SMS)
                    .build();

            orderRepository.saveAll(List.of(order1, order2));

            logger.info("Orders initialized successfully.");
        } catch (Exception e) {
            logger.error("Error initializing orders: " + e.getMessage());
            e.printStackTrace();
        }
    }
}