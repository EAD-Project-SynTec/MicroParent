package org.example.Services.orders;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.*;
import org.example.Models.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServices {
    private final OrderCreator orderCreationService;
    private final OrderMessager orderMessageService;
    private final OrderValidator orderValidationService;
    private final OrderRetriever orderRetriveService;
    private final OrderUpdator orderUpdateService;

    public void placeOrder(OrderRequestDto orderRequest) {
        try {
            orderValidationService.validateOrderAvailability(orderRequest);
            orderCreationService.saveOrder(orderRequest);
            orderValidationService.updateInventory(orderRequest);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Order> getOrders() {
        return orderRetriveService.getAllOrders();
    }

    public boolean updateOrderStatus(String orderId, String status) {
       return orderUpdateService.updateOrderStatus(orderId, status);
    }

    // Fetch orders by user ID
    public List<OrderWithDetailsDto> getOrdersByUserId(String userId) {
        return orderRetriveService.getOrderList(userId);
    }

    // Fetch orders to seller
    public List<OrderWithDetailsDto> getAllOrders() {
        return orderRetriveService.getOrderList();
    }

    // Fetch full order details by order ID including product details
    public OrderDetailsDto getFullOrderById(String orderId) {
        return orderRetriveService.getOrderDetails(orderId);
    }

    // create order with RabbitMQ
    public void createOrder(OrderDto orderRequest) {
        // Send order message to the queue
        orderMessageService.sendOrderMessage(orderRequest);
        // Receive the order response from the queue
        var response = orderMessageService.receiveOrderResponse();
        if (response != null && response.isAvailable()) {
            // orderCreationService.saveOrder(orderRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is failed , product is out of stock");
        }
    }

}
