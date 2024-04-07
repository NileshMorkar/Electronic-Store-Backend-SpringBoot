package com.example.ElectronicStore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartDto {

    private Long id;
    private Date createdAt;

    private UserResponse user;

    private List<CartItemDto> items = new ArrayList<>();
}
