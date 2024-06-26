package com.example.ElectronicStore.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartItemDto {
    private Long id;

    private ProductResponse product;

    private int quantity;
    private int totalPrice;
}
