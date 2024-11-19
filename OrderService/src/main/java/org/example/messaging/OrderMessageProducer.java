package org.example.messaging;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.OrderMessage;
import org.example.Models.Order;
import org.example.Models.OrderItem;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderMessageProducer
{
    private final RabbitTemplate rabbitTemplate;

    public void sendOrderMessage(Order order){
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setId(order.getId());
        orderMessage.setStatus(order.getStatus());
        orderMessage.setDateCreated(order.getDateCreated());
        orderMessage.setUserId(order.getUserId());
        orderMessage.setItems(order.getItems());
        rabbitTemplate.convertAndSend("orderQueue", orderMessage);
    }

}
