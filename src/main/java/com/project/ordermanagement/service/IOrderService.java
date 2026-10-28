package com.project.ordermanagement.service;

import com.project.ordermanagement.dto.OrderDTO;
import com.project.ordermanagement.dto.UpdateStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface IOrderService {
    OrderDTO create(OrderDTO orderDTO);
    OrderDTO getById(Long id);
    Page<OrderDTO> getFilteredOrders(String status, Pageable pageable);
    BigDecimal getTotalFor(String status);
    OrderDTO updateOrderStatus(Long id, UpdateStatusDTO request);
}
