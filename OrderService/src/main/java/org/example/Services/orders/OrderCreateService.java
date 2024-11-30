package org.example.Services.orders;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.OrderDto;
import org.example.Dtos.OrderRequestDto;
import org.example.Models.Order;
import org.example.Models.OrderItem;
import org.example.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class OrderCreateService implements  OrderCreator{
    private final OrderRepository orderRepository;

    @Override
    public void saveOrder(OrderRequestDto orderRequest) {
        Order order = Order.builder()
                .userId(orderRequest.getUserId())
                .address(orderRequest.getAddress())
                .dateCreated(orderRequest.getDateCreated())
                .items(orderRequest.getItems().stream()
                        .map(item -> new OrderItem(item.getProductID(), item.getQuantity(), item.getPrice() , item.getProductName()))
                        .collect(Collectors.toList()))
                .build();
        orderRepository.save(order);
    }
}
