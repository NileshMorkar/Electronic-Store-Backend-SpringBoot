package com.example.ElectronicStore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemDto {


    private Long id;

    private int quantity;

    private int totalPrice;

    private ProductResponse product;


}
