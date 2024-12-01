package org.example.Services.orders;

import org.example.Dtos.OrderRequestDto;

public interface OrderCreator {
    void saveOrder(OrderRequestDto orderRequest);
}
