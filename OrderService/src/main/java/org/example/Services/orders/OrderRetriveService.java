package org.example.Services.orders;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.OrderDetailsDto;
import org.example.Dtos.OrderItemDto;
import org.example.Dtos.OrderWithDetailsDto;
import org.example.Models.Order;
import org.example.Models.OrderItem;
import org.example.Repository.OrderRepository;
import org.example.Services.product.ProductDetailsService;
import org.example.client.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderRetriveService implements OrderRetriever {
    private final OrderRepository orderRepository;
    private final ProductDetailsService productDetailsService;

    @Override
    public OrderDetailsDto getOrderDetails(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        List<OrderItemDto> orderItemDtos = order.getItems().stream()
                .map(orderItem -> {
                    var product = productDetailsService.getProductDetails(orderItem.getProductID());
                    return new OrderItemDto(orderItem.getProductID(), product.getName(), product.getDescription(),
                            orderItem.getQuantity(), product.getPrice(), product.getImageUrl(), product.getCategory());
                })
                .collect(Collectors.toList());

        return OrderDetailsDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .dateCreated(order.getDateCreated())
                .status(order.getStatus())
                .items(orderItemDtos)
                .build();
    }

    @Override
    public List<OrderWithDetailsDto> getOrderList(String userId) {
        // Retrieve orders by userId
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getUserId().equals(userId))
                .toList();

        return orders.stream()
                .map(order -> OrderWithDetailsDto.builder()
                        .orderId(order.getId())
                        .userId(order.getUserId())
                        .dateCreated(order.getDateCreated())
                        .status(order.getStatus())
                        .items(order.getItems().stream()
                                .map(this::mapOrderItemToDetails)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public List<OrderWithDetailsDto> getOrderList() {
        // Retrieve orders by userId
        List<Order> orders = orderRepository.findAll().stream().toList();

        return orders.stream()
                .map(order -> OrderWithDetailsDto.builder()
                        .orderId(order.getId())
                        .userId(order.getUserId())
                        .dateCreated(order.getDateCreated())
                        .status(order.getStatus())
                        .items(order.getItems().stream()
                                .map(this::mapOrderItemToDetails)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private OrderWithDetailsDto.OrderItemDetailsDto mapOrderItemToDetails(OrderItem orderItem) {
        // Fetch product details using ProductDetailsService
        Product product = productDetailsService.getProductDetails(orderItem.getProductID());

        return OrderWithDetailsDto.OrderItemDetailsDto.builder()
                .productId(orderItem.getProductID())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .productName(product.getName())
                .productImageUrl(product.getImageUrl())
                .build();
    }

    @Override
    public List<Order> getUserOrders(String userId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }


    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


}
