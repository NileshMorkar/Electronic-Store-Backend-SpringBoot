package com.example.ElectronicStore.service.Impl;

import com.example.ElectronicStore.Helper.Helper;
import com.example.ElectronicStore.dto.OrderRequest;
import com.example.ElectronicStore.dto.OrderResponse;
import com.example.ElectronicStore.dto.PageableResponse;
import com.example.ElectronicStore.entity.Cart;
import com.example.ElectronicStore.entity.Order;
import com.example.ElectronicStore.entity.OrderItem;
import com.example.ElectronicStore.entity.UserEntity;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.repository.CartRepository;
import com.example.ElectronicStore.repository.OrderRepository;
import com.example.ElectronicStore.repository.UserRepository;
import com.example.ElectronicStore.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        UserEntity user = userRepository.findById(orderRequest.getUserId()).orElseThrow(() -> new GlobalException("User Not Found", HttpStatus.NOT_FOUND));
        Cart cart = cartRepository.findById(orderRequest.getCartId()).orElseThrow(() -> new GlobalException("Cart Not Found", HttpStatus.NOT_FOUND));


        if (cart.getItems().isEmpty()) {
            throw new GlobalException("Cart Is Empty !!", HttpStatus.BAD_REQUEST);
        }


        Order order = Order.builder()
                .orderedDate(new Date())
                .orderStatus("PENDING")
                .paymentStatus(orderRequest.getPaymentStatus())
                .user(user)
                .billingName(orderRequest.getBillingName())
                .billingPhone(orderRequest.getBillingPhone())
                .billingAddress(orderRequest.getBillingAddress())
                .build();

        AtomicReference<Integer> sum = new AtomicReference<>(0);

        List<OrderItem> orderItems = cart.getItems().stream().map(
                cartItem -> {
                    sum.set(sum.get() + cartItem.getTotalPrice());
                    return OrderItem
                            .builder()
                            .order(order)
                            .quantity(cartItem.getQuantity())
                            .product(cartItem.getProduct())
                            .totalPrice(cartItem.getTotalPrice())
                            .build();

                }
        ).toList();

        order.setOrderAmount(sum.get());
        order.setOrderItems(orderItems);

        cart.getItems().clear();
        cartRepository.save(cart);

        orderRepository.save(order);

        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public void removeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new GlobalException("Order Not Found!!", HttpStatus.NOT_FOUND));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderResponse> getAllOrdersOfUser(Long userId) {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Not Found !!", HttpStatus.NOT_FOUND));

        List<Order> orderList = orderRepository.findByUser(userEntity);

        return orderList.stream().map(order -> modelMapper.map(order, OrderResponse.class)).toList();
    }

    @Override
    public PageableResponse<OrderResponse> getAllOrders(String pageNumber, String pageSize, String sortBy, String sortDir) {

        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            System.out.println("GHjjjjjjjjjjjjjjjjj");
            PageRequest pageRequest = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), sort);

            System.out.println("GHjjiiiiiiiiiiiiijjjjjjjjj");
            Page<Order> page = orderRepository.findAll(pageRequest);
            return Helper.getPageableResponse(page, OrderResponse.class);
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
