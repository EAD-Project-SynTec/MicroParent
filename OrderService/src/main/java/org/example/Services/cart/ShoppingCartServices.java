package org.example.Services.cart;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AddToCartDto;
import org.example.Dtos.RemoveItemDto;
import org.example.Models.ShoppingCart;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ShoppingCartServices {
    final AddToCartService addToCartService;
    final GetCartService getCartService;
    final RemoveCartItemService removeCartItemService;

    public void addToCart(AddToCartDto addToCartDto) {
        addToCartService.addToCart(addToCartDto);
    }

    public ShoppingCart getCart(String email) {
        return getCartService.getCart(email);
    }

    public boolean removeItemFromCart(RemoveItemDto removeItemDto) {
        return removeCartItemService.removeItemFromCart(removeItemDto);
    }
}
