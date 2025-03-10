package com.example.order_service.service;

import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderEntity;
import com.example.order_service.model.OrderEvent;
import com.example.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private static final String ORDER_EXCHANGE = "orderExchange";
    private static final String ORDER_EVENTS_ROUTING_KEY = "order.events";

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final RabbitTemplate rabbitTemplate;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Autowired
    public OrderService(RabbitTemplate rabbitTemplate, OrderRepository orderRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        OrderEntity savedOrderEntity = saveOrderEntity(order);
        Order createdOrder = orderMapper.toDto(savedOrderEntity);
        publishOrderEvent(createdOrder.getId(), "CREATE", createdOrder);
        return createdOrder;
    }

    @Transactional
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::mapAndPublishGetEvent);
    }

    @Transactional
    public void deleteOrder(Long id) {
        OrderEntity orderEntity = findOrderEntityById(id);
        orderRepository.deleteById(id);
        publishOrderEvent(id, "DELETE", orderMapper.toDto(orderEntity));
    }

    private OrderEntity saveOrderEntity(Order order) {
        OrderEntity orderEntity = orderMapper.toEntity(order);
        return orderRepository.save(orderEntity);
    }

    private Order mapAndPublishGetEvent(OrderEntity orderEntity) {
        Order order = orderMapper.toDto(orderEntity);
        publishOrderEvent(order.getId(), "GET", order);
        return order;
    }

    private OrderEntity findOrderEntityById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private void publishOrderEvent(Long orderId, String eventType, Order order) {
        OrderEvent event = new OrderEvent(orderId, eventType, order);
        publishEventToRabbitMQ(event);
    }

    private void publishEventToRabbitMQ(OrderEvent event) {
        logger.info("📤 Sending message to RabbitMQ: {}", event);
        try {
            rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_EVENTS_ROUTING_KEY, event);
            logger.info("📤 Event successfully sent to RabbitMQ");
        } catch (Exception e) {
            logger.error("❌ Failed to send message to RabbitMQ: {}", e.getMessage(), e);
        }
    }
}
