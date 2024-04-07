package com.example.ElectronicStore.controller;


import com.example.ElectronicStore.dto.ApiResponseMessage;
import com.example.ElectronicStore.dto.OrderRequest;
import com.example.ElectronicStore.dto.OrderResponse;
import com.example.ElectronicStore.dto.PageableResponse;
import com.example.ElectronicStore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable Long orderId) {
        orderService.removeOrder(orderId);

        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .message("Order Remove Successfully!!")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersOfUser(@PathVariable Long userId) {
        List<OrderResponse> orderResponseList = orderService.getAllOrdersOfUser(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseList);
    }

    @GetMapping("/all")
    public ResponseEntity<PageableResponse<OrderResponse>> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) String pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize,
            @RequestParam(name = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir

    ) {
        PageableResponse<OrderResponse> pageableResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.status(HttpStatus.CREATED).body(pageableResponse);
    }
}
