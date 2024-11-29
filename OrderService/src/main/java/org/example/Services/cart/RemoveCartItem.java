package org.example.Services.cart;
import org.example.Dtos.RemoveItemDto;

public interface RemoveCartItem {
    boolean removeItemFromCart(RemoveItemDto removeItemDto);
}
