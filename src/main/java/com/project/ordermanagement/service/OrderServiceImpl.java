package com.project.ordermanagement.service;

import com.project.ordermanagement.dao.OrderRepository;
import com.project.ordermanagement.domain.Order;
import com.project.ordermanagement.domain.enums.OrderStatus;
import com.project.ordermanagement.dto.OrderDTO;
import com.project.ordermanagement.dto.UpdateStatusDTO;
import com.project.ordermanagement.exception.EntityNotFoundException;
import com.project.ordermanagement.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements IOrderService{

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        Order order = OrderMapper.requestToOrder(orderDTO);
        Order savedOrder = orderRepository.save(order);

        return OrderMapper.orderToRequest(savedOrder);
    }

    @Override
    public OrderDTO getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The id is null");
        }
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("The order you requested is not present in the database");
        }
        return OrderMapper.orderToRequest(orderOptional.get());
    }

    public Page<OrderDTO> getFilteredOrders(String status, Pageable pageable) {
        Page<Order> orderPage;

        if (status != null && !status.trim().isEmpty()) {
            OrderStatus orderStatus;
            try {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Status invalid: " + status + ". Status must be: PENDING, COMPLETED or CANCELLED.");
            }
            orderPage = orderRepository.findByStatus(orderStatus, pageable);
        } else {
            orderPage = orderRepository.findAll(pageable);
        }

        return orderPage.map(OrderMapper::orderToRequest);
    }

    public BigDecimal getTotalFor(String status) {
        List<Order> orders;
        if (status != null && !status.trim().isEmpty()) {
            OrderStatus orderStatus;
            try {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Status invalid: " + status + ". Status must be: PENDING, COMPLETED or CANCELLED.");
            }
            orders = orderRepository.findByStatus(orderStatus);
            return orders.stream()
                    .map(Order::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    public OrderDTO updateOrderStatus(Long id, UpdateStatusDTO request) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("The order you requested is not present in the database");
        }
        Order order = orderOptional.get();

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        if (currentStatus.equals(newStatus)) {
            return OrderMapper.orderToRequest(order);
        }

        // Regula: O comandă COMPLETED sau CANCELLED nu mai poate trece într-un alt status
        if (currentStatus == OrderStatus.COMPLETED || currentStatus == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("The current command status: " + currentStatus + " cannot be modified.");
        }

        // Regula: PENDING poate deveni doar COMPLETED sau CANCELLED
        if (currentStatus == OrderStatus.PENDING) {
            if (newStatus != OrderStatus.COMPLETED && newStatus != OrderStatus.CANCELLED) {
                throw new IllegalArgumentException("A PENDING command can be modified in COMPLETED or CANCELLED.");
            }
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return OrderMapper.orderToRequest(updatedOrder);
    }
}
