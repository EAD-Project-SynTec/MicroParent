package org.example.Services.orders;

import org.example.Dtos.OrderDetailsDto;
import org.example.Dtos.OrderWithDetailsDto;
import org.example.Models.Order;

import java.util.List;

public interface OrderRetriever {
    OrderDetailsDto getOrderDetails(String orderId);
    List<Order> getUserOrders(String userId);
    List<Order> getAllOrders();
    List<OrderWithDetailsDto> getOrderList(String userId);
    List<OrderWithDetailsDto> getOrderList();
}
