package com.example.ElectronicStore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank
    private String name;
    private String description;
    private double price;
    private int discount;
    private int quantity;
    private boolean isLive;
    private boolean isInStock;

    private String image;
    private CategoryResponse category;
}
