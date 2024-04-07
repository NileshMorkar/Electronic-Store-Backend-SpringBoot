package com.example.ElectronicStore.service;

import com.example.ElectronicStore.dto.OrderRequest;
import com.example.ElectronicStore.dto.OrderResponse;
import com.example.ElectronicStore.dto.PageableResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);


    void removeOrder(Long orderId);


    List<OrderResponse> getAllOrdersOfUser(Long userId);

    PageableResponse<OrderResponse> getAllOrders(String pageNumber, String pageSize, String sortBy, String sortDir);
}
