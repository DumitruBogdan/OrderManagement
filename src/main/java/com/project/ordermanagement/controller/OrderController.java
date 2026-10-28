package com.project.ordermanagement.controller;

import com.project.ordermanagement.dto.OrderDTO;
import com.project.ordermanagement.dto.UpdateStatusDTO;
import com.project.ordermanagement.service.IOrderService;
import com.project.ordermanagement.service.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    private final IOrderService orderService;

    @Autowired
    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderService = orderServiceImpl;
    }

    @PostMapping
    public OrderDTO create(@RequestBody @Valid OrderDTO orderDTO){
        return orderService.create(orderDTO);
    }

    @GetMapping("/{id}")
    public OrderDTO getById(@PathVariable Long id){
        return orderService.getById(id);
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getFilteredOrders(@RequestParam(required = false) String status,
                                                            Pageable pageable) {
        Page<OrderDTO> orders = orderService.getFilteredOrders(status, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotal(@RequestParam(required = false) String status) {
        BigDecimal total = orderService.getTotalFor(status);
        return ResponseEntity.ok(total);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusDTO request) {

        OrderDTO updatedOrder = orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok(updatedOrder);
    }
}
