package com.example.ElectronicStore.service;

import com.example.ElectronicStore.dto.ApiResponseMessage;
import com.example.ElectronicStore.dto.PageableResponse;
import com.example.ElectronicStore.dto.UserRequest;
import com.example.ElectronicStore.dto.UserResponse;
import com.example.ElectronicStore.exception.GlobalException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<UserResponse> createNormalUser(UserRequest userRequest);

    ResponseEntity<ApiResponseMessage> delete(Long userId) throws GlobalException;


    ResponseEntity<PageableResponse<UserResponse>> getAll(String pageNumber, String pageSize, String sortBy, String sortDir);

    ResponseEntity<ApiResponseMessage> update(Long userId, UserRequest newUser) throws GlobalException;

    UserResponse getById(Long userId) throws GlobalException;

    ResponseEntity<UserResponse> getByEmail(String userEmail) throws GlobalException;

    ResponseEntity<List<UserResponse>> search(String keyword);
}
