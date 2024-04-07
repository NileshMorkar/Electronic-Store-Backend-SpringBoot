package com.example.ElectronicStore.controller;

import com.example.ElectronicStore.dto.AddItemToCartRequest;
import com.example.ElectronicStore.dto.ApiResponseMessage;
import com.example.ElectronicStore.dto.CartDto;
import com.example.ElectronicStore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable Long userId, @RequestBody AddItemToCartRequest cartRequest) {
        CartDto cartDto = cartService.addItemToCart(userId, cartRequest);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long userId, @PathVariable Long itemId) {
        CartDto cartDto = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .message("Cart Cleared Successfully!")
                .httpStatus(HttpStatus.OK)
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable Long userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

}
