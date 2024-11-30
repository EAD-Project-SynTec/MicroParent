package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AddToCartDto;
import org.example.Dtos.RemoveItemDto;
import org.example.Models.ShoppingCart;
import org.example.Services.cart.ShoppingCartServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ShoppingCartController {

    private final ShoppingCartServices shoppingCartServices;

    @PostMapping
    public ResponseEntity<String> AddToCart(@RequestBody AddToCartDto addToCartDto) {
        try {
            shoppingCartServices.addToCart(addToCartDto);
            return ResponseEntity.status(HttpStatus.OK).body("Item added to the cart");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> GetCart(@RequestParam("email") String email) {
        try {
            ShoppingCart shoppingCart= shoppingCartServices.getCart(email);
            return ResponseEntity.status(HttpStatus.OK).body(shoppingCart);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> RemoveItem(@RequestBody RemoveItemDto removeItemDto){
        try {
            boolean isRemoved = shoppingCartServices.removeItemFromCart(removeItemDto);

            if (isRemoved) {
                return ResponseEntity.ok("Item removed from cart successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in cart or cart does not exist");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
