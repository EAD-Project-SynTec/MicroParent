package com.example.ProductService.messaging;

import com.example.ProductService.Dtos.OrderMessage;
import com.example.ProductService.Services.product.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMessageConsumer {
    private final ProductServices productServices;
    @RabbitListener(queues="orderQueue")
    public void consumeOrderMessage(OrderMessage orderMessage) {
        productServices.updateProductInventory(orderMessage);
    }
}
