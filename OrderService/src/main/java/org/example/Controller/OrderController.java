package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.*;
import org.example.Models.Order;
import org.example.Services.orders.OrderServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController {
    private final OrderServices orderServices;

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<OrderWithDetailsDto> getOrders() {
        return orderServices.getAllOrders();
    }

    // create simple order
    @PostMapping("create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody OrderDto orderRequestDto) {
        orderServices.createOrder(orderRequestDto);
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        try {
            orderServices.placeOrder(orderRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order creation failed: " + e.getMessage());
        }
    }


    // update order status via url
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        boolean updated = orderServices.updateOrderStatus(id, status);
        if (updated) {
            return ResponseEntity.ok("Order status updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // update order status via json body
    @PutMapping("/status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) {
        boolean updated = orderServices.updateOrderStatus(request.getId(), request.getStatus());
        if (updated) {
            return ResponseEntity.ok("Order status updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get orders by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderWithDetailsDto>> getOrdersByUserId(@PathVariable String userId) {
        List<OrderWithDetailsDto> orders = orderServices.getOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    // Get full order information by order ID (including product details)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsDto> getFullOrderDetails(@PathVariable String orderId) {
        OrderDetailsDto orderDetails = orderServices.getFullOrderById(orderId);
        return ResponseEntity.ok(orderDetails);
    }
}
