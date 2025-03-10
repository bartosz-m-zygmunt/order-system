package com.example.order_service.service;

import com.example.order_service.enumeration.NotificationType;
import com.example.order_service.enumeration.StatusType;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderEntity;
import com.example.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private Order orderDTO;
    private OrderEntity orderEntity;
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @BeforeEach
    public void setUp() {
        orderDTO = Order.builder()
                .orderNumber(1001L)
                .customerName("John Doe")
                .statusType(StatusType.SHIPPED)
                .price(new BigDecimal("199.99"))
                .notificationType(NotificationType.EMAIL)
                .build();

        orderEntity = OrderEntity.builder()
                .orderNumber(1001L)
                .customerName("John Doe")
                .statusType(StatusType.SHIPPED)
                .price(new BigDecimal("199.99"))
                .notificationType(NotificationType.EMAIL)
                .build();
    }

    @Test
    public void shouldCreateOrder() {
        // Given
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        // When
        Order result = orderService.createOrder(orderDTO);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(StatusType.SHIPPED, result.getStatusType());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(rabbitTemplate, times(1)).convertAndSend(eq("orderExchange"), eq("order.events"), any(Message.class));
    }

    @Test
    public void shouldReturnOrderWhenFoundById() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));

        // When
        Optional<Order> result = orderService.getOrderById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getCustomerName());
        verify(orderRepository, times(1)).findById(1L);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("orderExchange"), eq("order.events"), any(Message.class));
    }

    @Test
    public void shouldDeleteOrderWhenFound() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        doNothing().when(orderRepository).deleteById(1L);

        // When
        orderService.deleteOrder(1L);

        // Then
        verify(orderRepository, times(1)).deleteById(1L);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("orderExchange"), eq("order.events"), any(Message.class));
    }

    @Test
    public void shouldThrowExceptionWhenOrderNotFound() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1L));
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Message.class));
    }
}
