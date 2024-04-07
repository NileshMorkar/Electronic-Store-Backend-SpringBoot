package com.example.ElectronicStore.service.Impl;

import com.example.ElectronicStore.dto.*;
import com.example.ElectronicStore.entity.Cart;
import com.example.ElectronicStore.entity.CartItem;
import com.example.ElectronicStore.entity.ProductEntity;
import com.example.ElectronicStore.entity.UserEntity;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.repository.CartItemRepository;
import com.example.ElectronicStore.repository.CartRepository;
import com.example.ElectronicStore.repository.ProductRepository;
import com.example.ElectronicStore.repository.UserRepository;
import com.example.ElectronicStore.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CartRepository cartRepository;

    @Override
    public CartDto addItemToCart(Long userId, AddItemToCartRequest itemRequest) {

        Long productId = itemRequest.getProductId();
        int quantity = itemRequest.getQuantity();

        if (quantity <= 0) {
            throw new GlobalException("Quantity Must Be Greater Than 0 !!", HttpStatus.BAD_REQUEST);
        }

        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> new GlobalException("Product Not Found !!", HttpStatus.BAD_REQUEST));
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Not Found !!", HttpStatus.BAD_REQUEST));

        Cart cart = cartRepository.findByUser(userEntity).orElse(null);


        if (cart == null) {
            cart = new Cart();
            cart.setCreatedAt(new Date());
            cart.setUser(userEntity);
        }

        int totalPrice = (int) (productEntity.getPrice() * productEntity.getDiscount() / 100) * quantity;

        List<CartItem> items = cart.getItems();

        AtomicBoolean isPresent = new AtomicBoolean(false);
        items.forEach(
                cartItem -> {
                    if (Objects.equals(cartItem.getProduct().getId(), productId)) {
                        cartItem.setQuantity(quantity);
                        cartItem.setTotalPrice(totalPrice);
                        isPresent.set(true);
                    }
                }
        );

        if (!isPresent.get()) {

            CartItem cartItem = CartItem
                    .builder()
                    .cart(cart)
                    .product(productEntity)
                    .quantity(quantity)
                    .totalPrice(totalPrice)
                    .build();

            items.add(cartItem);
        }

        cartRepository.save(cart);

        List<CartItemDto> itemDtoList = cart.getItems().stream().map(
                cartItem -> CartItemDto
                        .builder()
                        .totalPrice(cartItem.getTotalPrice())
                        .quantity(cartItem.getQuantity())
                        .id(cartItem.getId())
                        .product(modelMapper.map(cartItem.getProduct(), ProductResponse.class))
                        .build()
        ).toList();

//        return modelMapper.map(cart, CartDto.class);

        return CartDto
                .builder()
                .items(itemDtoList)
                .user(modelMapper.map(userEntity, UserResponse.class))
                .id(cart.getId())
                .createdAt(cart.getCreatedAt())
                .build();
    }

    @Override
    public CartDto removeItemFromCart(Long userId, Long cartItemId) {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Not Found !!", HttpStatus.BAD_REQUEST));
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new GlobalException("Product Not Present In Cart !!", HttpStatus.BAD_REQUEST));

        cartItemRepository.delete(cartItem);

        Cart cart = cartRepository.findByUser(userEntity).orElseThrow(() -> new GlobalException("Cart Not Found !!", HttpStatus.BAD_REQUEST));
        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public void clearCart(Long userId) {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Not Found !!", HttpStatus.BAD_REQUEST));
        Cart cart = cartRepository.findByUser(userEntity).orElseThrow(() -> new GlobalException("Cart Not Found !!", HttpStatus.NOT_FOUND));

        cart.getItems().clear();

        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(Long userId) {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Not Found !!", HttpStatus.BAD_REQUEST));
        Cart cart = cartRepository.findByUser(userEntity).orElseThrow(() -> new GlobalException("Cart Not Found !!", HttpStatus.NOT_FOUND));

        return modelMapper.map(cart, CartDto.class);

    }
}
