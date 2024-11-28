package com.example.ProductService.messaging;

import com.example.ProductService.Dtos.OrderMessage;
import com.example.ProductService.Dtos.OrderResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderResponseProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendOrderResponse(OrderMessage order , Boolean available) {
        OrderResponseMessage orderResponse = new OrderResponseMessage();
        orderResponse.setId(order.getId());
        orderResponse.setUserId(order.getUserId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setItems(order.getItems());
        orderResponse.setDateCreated(order.getDateCreated());
        orderResponse.setAvailable(available);
        rabbitTemplate.convertAndSend("responseQueue", orderResponse);
    }
}
