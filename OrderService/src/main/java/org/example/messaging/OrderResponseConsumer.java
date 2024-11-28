package org.example.messaging;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.OrderResponse;
import org.example.Models.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@RequiredArgsConstructor
public class OrderResponseConsumer {
    private final BlockingQueue<OrderResponse> responseQueue = new LinkedBlockingQueue<>();

    @RabbitListener(queues = "responseQueue")
    public void receiveOrderMessage(OrderResponse orderResponse) {
        try {
            responseQueue.put(orderResponse);
            System.out.println("Received Order Response: " + orderResponse);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to process order response", e);
        }
    }

    public OrderResponse receiveOrder() {
        try {
            // Retrieve and return the message from the queue
            return responseQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to retrieve order response", e);
        }
    }
}
