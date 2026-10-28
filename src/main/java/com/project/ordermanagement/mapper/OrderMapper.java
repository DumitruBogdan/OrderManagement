package com.project.ordermanagement.mapper;

import com.project.ordermanagement.domain.Order;
import com.project.ordermanagement.dto.OrderDTO;

public class OrderMapper {
    public static Order requestToOrder(OrderDTO orderDTO){
        return Order.builder()
                .id(orderDTO.getId())
                .customerName(orderDTO.getCustomerName())
                .amount(orderDTO.getAmount())
                .status(orderDTO.getStatus())
                .createdAt(orderDTO.getCreatedAt())
                .updatedAt(orderDTO.getUpdatedAt())
                .build();
    }

    public static OrderDTO orderToRequest(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .amount(order.getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}