package com.example.ElectronicStore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int discount;
    private int quantity;
    private Date date;
    private boolean isLive;
    private boolean isInStock;
    private String image;
    private CategoryResponse category;
}

