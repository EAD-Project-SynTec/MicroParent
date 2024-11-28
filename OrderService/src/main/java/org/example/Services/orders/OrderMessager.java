package org.example.Services.orders;

import org.example.Dtos.OrderDto;
import org.example.Dtos.OrderResponse;

public interface OrderMessager {
    void sendOrderMessage(OrderDto orderRequest);
    OrderResponse receiveOrderResponse();
}
