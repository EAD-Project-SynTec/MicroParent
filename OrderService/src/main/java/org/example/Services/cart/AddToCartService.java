package org.example.Services.cart;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AddToCartDto;
import org.example.Models.CartItem;
import org.example.Models.ShoppingCart;
import org.example.Repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddToCartService implements AddToCart{
    final ShoppingCartRepository shoppingCartRepository;
    @Override
    public void addToCart(AddToCartDto addToCartDto) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByCustomerEmail(addToCartDto.getCustomerEmail());
        ShoppingCart shoppingCart;
        if (optionalCart.isPresent()) {
            shoppingCart = optionalCart.get();

            CartItem newItem = CartItem.builder()
                    .productID(addToCartDto.getProductID())
                    .quantity(addToCartDto.getQuantity())
                    .price(addToCartDto.getPrice())
                    .name(addToCartDto.getName())
                    .imageUrl(addToCartDto.getImageUrl())
                    .build();

            List<CartItem> existingItems = shoppingCart.getCartItems();
            if (existingItems == null) {
                existingItems = new ArrayList<>();
            }

            existingItems.add(newItem);
            shoppingCart.setCartItems(existingItems);

        }
        else {
            CartItem newItem = CartItem.builder()
                    .productID(addToCartDto.getProductID())
                    .quantity(addToCartDto.getQuantity())
                    .price(addToCartDto.getPrice())
                    .name(addToCartDto.getName())
                    .imageUrl(addToCartDto.getImageUrl())
                    .build();

            List<CartItem> items = new ArrayList<>();
            items.add(newItem);

            shoppingCart = ShoppingCart.builder()
                    .customerEmail(addToCartDto.getCustomerEmail())
                    .cartItems(items)
                    .build();
        }
        shoppingCartRepository.save(shoppingCart);
    }
}
