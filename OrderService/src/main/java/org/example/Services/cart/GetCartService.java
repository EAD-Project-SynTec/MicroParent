package org.example.Services.cart;

import lombok.RequiredArgsConstructor;
import org.example.Models.ShoppingCart;
import org.example.Repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetCartService implements GetCart{
    final ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart getCart(String email) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByCustomerEmail(email);
        return optionalCart.orElse(null);
    }
}
