package org.example.Services.orders;

public interface OrderUpdator {
    boolean updateOrderStatus(String orderId, String status);
}
