package com.example.ElectronicStore.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Long userId;
    private Long cartId;

    private String paymentStatus;
    private String billingAddress;
    private String billingPhone;
    private String billingName;

}
