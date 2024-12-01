package org.example.Services.cart;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.RemoveItemDto;
import org.example.Models.CartItem;
import org.example.Models.ShoppingCart;
import org.example.Repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemoveCartItemService implements RemoveCartItem{
    final ShoppingCartRepository shoppingCartRepository;
    @Override
    public boolean removeItemFromCart(RemoveItemDto removeItemDto) {
        ShoppingCart shoppingCart;
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByCustomerEmail(removeItemDto.getCustomerEmail());
        if (optionalCart.isPresent()) {
            shoppingCart = optionalCart.get();
            List<CartItem> cartItems = shoppingCart.getCartItems();
            boolean removed = cartItems.removeIf(item -> item.getProductID() == removeItemDto.getItemID());

            if (removed) {
                shoppingCartRepository.save(shoppingCart);
                return true;
            }
        }
        return false;
    }
}
