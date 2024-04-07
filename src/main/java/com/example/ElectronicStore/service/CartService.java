package com.example.ElectronicStore.service;

import com.example.ElectronicStore.dto.AddItemToCartRequest;
import com.example.ElectronicStore.dto.CartDto;

public interface CartService {

    CartDto addItemToCart(Long userId, AddItemToCartRequest itemRequest);

    CartDto removeItemFromCart(Long userId, Long cartItemId);

    void clearCart(Long userId);

    CartDto getCartByUser(Long userId);
}
