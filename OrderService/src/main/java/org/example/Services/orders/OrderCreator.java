package org.example.Services.orders;

import org.example.Dtos.OrderDto;

public interface OrderCreator {
    void saveOrder(OrderDto orderRequest);
}
