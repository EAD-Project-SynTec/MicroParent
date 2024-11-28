package org.example.Services.messaging;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.OrderDto;
import org.example.Dtos.OrderMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderMessageProducer
{
    private final RabbitTemplate rabbitTemplate;

    public void sendOrderMessage(OrderDto order){
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setId(order.getId());
        orderMessage.setStatus(order.getStatus());
        orderMessage.setDateCreated(order.getDateCreated());
        orderMessage.setUserId(order.getUserId());
        orderMessage.setItems(order.getItems());
        rabbitTemplate.convertAndSend("orderQueue", orderMessage);
    }

}
