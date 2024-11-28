package org.example.Services.orders;

import lombok.RequiredArgsConstructor;
import org.example.Models.Order;
import org.example.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderUpdateService implements  OrderUpdator {
    private  final OrderRepository orderRepository;

    @Override
    public boolean updateOrderStatus(String orderId, String status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            orderRepository.save(order);
            return true;
        }
        return false;
    }
}
