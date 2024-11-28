package org.example.Services.orders;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.OrderDto;
import org.example.Dtos.OrderResponse;
import org.example.Services.messaging.OrderMessageProducer;
import org.example.Services.messaging.OrderResponseConsumer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMessageService implements OrderMessager {
    private final OrderMessageProducer orderMessageProducer;
    private final OrderResponseConsumer orderResponseConsumer;

    @Override
    public void sendOrderMessage(OrderDto orderRequest) {
        orderMessageProducer.sendOrderMessage(orderRequest);
    }

    @Override
    public OrderResponse receiveOrderResponse() {
        return orderResponseConsumer.receiveOrder();
    }
}
