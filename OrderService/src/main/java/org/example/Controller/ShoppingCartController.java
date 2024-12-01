package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AddToCartDto;
import org.example.Dtos.RemoveItemDto;
import org.example.Models.ShoppingCart;
import org.example.Services.cart.ShoppingCartServices;
import org.springframework.dao.DataAccessException;
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
            return ResponseEntity.status(HttpStatus.CREATED).body("Item added to the cart");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input provided: " + e.getMessage());
        }catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> GetCart(@RequestParam("email") String email) {
        try {
            ShoppingCart shoppingCart= shoppingCartServices.getCart(email);
            return ResponseEntity.status(HttpStatus.OK).body(shoppingCart);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input provided: " + e.getMessage());
        }catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
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
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input provided: " + e.getMessage());
        }catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> RemoveAllItems(@RequestParam("email") String email) {
        try{
            boolean isAllRemoved = shoppingCartServices.removeAllItemsFromCart(email);
            if (isAllRemoved) {
                return ResponseEntity.ok("All items removed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart is already empty");
            }
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input provided: " + e.getMessage());
        }catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
    
}
