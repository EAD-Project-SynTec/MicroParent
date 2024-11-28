package org.example.Services.orders;

import org.example.Dtos.OrderRequestDto;

public interface OrderValidator {
    void validateOrderAvailability(OrderRequestDto orderRequest);
}
