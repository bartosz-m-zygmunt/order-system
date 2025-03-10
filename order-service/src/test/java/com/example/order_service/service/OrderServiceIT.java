package com.example.order_service.service;

import com.example.order_service.enumeration.NotificationType;
import com.example.order_service.model.Order;
import com.example.order_service.enumeration.StatusType;
import com.example.order_service.model.OrderEntity;
import com.example.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class OrderServiceIT {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    private Order orderDTO;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();

        orderDTO = Order.builder()
                .orderNumber(1001L)
                .customerName("John Doe")
                .statusType(StatusType.SHIPPED)
                .price(new BigDecimal("199.99"))
                .notificationType(NotificationType.EMAIL)
                .build();
    }

    @Test
    public void shouldCreateOrder() {
        // When
        Order savedOrder = orderService.createOrder(orderDTO);

        // Then
        assertNotNull(savedOrder.getId());
        assertEquals("John Doe", savedOrder.getCustomerName());
        assertEquals(StatusType.SHIPPED, savedOrder.getStatusType());

        Optional<OrderEntity> entity = orderRepository.findById(savedOrder.getId());
        assertTrue(entity.isPresent());
        assertEquals("John Doe", entity.get().getCustomerName());

        //TODO add assertions for RabbitMQ
    }

    @Test
    public void shouldFindOrderById() {
        // Given
        Order savedOrder = orderService.createOrder(orderDTO);

        // When
        Optional<Order> foundOrder = orderService.getOrderById(savedOrder.getId());

        // Then
        assertTrue(foundOrder.isPresent());
        assertEquals("John Doe", foundOrder.get().getCustomerName());

        //TODO add assertions for RabbitMQ
    }

    @Test
    public void shouldDeleteOrder() {
        // Given
        Order savedOrder = orderService.createOrder(orderDTO);

        // When
        orderService.deleteOrder(savedOrder.getId());

        // Then
        Optional<OrderEntity> deletedOrder = orderRepository.findById(savedOrder.getId());
        assertFalse(deletedOrder.isPresent());

        //TODO add assertions for RabbitMQ
    }
}