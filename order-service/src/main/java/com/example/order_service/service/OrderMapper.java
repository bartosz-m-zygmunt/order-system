package com.example.order_service.service;

import com.example.order_service.model.Order;
import com.example.order_service.model.OrderEntity;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    OrderEntity toEntity(Order order);

    Order toDto(OrderEntity orderEntity);
}
