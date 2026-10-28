package com.project.ordermanagement.dto;

import com.project.ordermanagement.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusDTO {
    private OrderStatus status;
}
