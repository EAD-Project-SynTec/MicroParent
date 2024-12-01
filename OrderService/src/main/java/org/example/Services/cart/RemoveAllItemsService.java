package org.example.Services.cart;

import lombok.RequiredArgsConstructor;
import org.example.Models.ShoppingCart;
import org.example.Repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemoveAllItemsService implements RemoveAllItems{
    final ShoppingCartRepository shoppingCartRepository;

    @Override
    public boolean removeAllItemsFromCart(String email) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByCustomerEmail(email);
        if (optionalCart.isPresent()) {
            ShoppingCart shoppingCart = optionalCart.get();
            shoppingCart.getCartItems().clear();
            System.out.println(shoppingCart.getCartItems());
            shoppingCartRepository.save(shoppingCart);
            return true;
        }
        return false;
    }
}
