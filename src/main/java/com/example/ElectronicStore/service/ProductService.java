package com.example.ElectronicStore.service;

import com.example.ElectronicStore.dto.PageableResponse;
import com.example.ElectronicStore.dto.ProductRequest;
import com.example.ElectronicStore.dto.ProductResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {


    ProductResponse create(ProductRequest productRequest);

    ProductResponse getById(Long id);

    ResponseEntity<PageableResponse<ProductResponse>> getAll(String pageNumber, String pageSize, String sortBy, String sortDir);

    ResponseEntity<PageableResponse<ProductResponse>> getAllLive(String pageNumber, String pageSize, String sortBy, String sortDir);

    void update(Long id, ProductRequest productRequest);

    void delete(Long id);

    ResponseEntity<PageableResponse<ProductResponse>> searchByName(String name, String pageNumber, String pageSize, String sortBy, String sortDir);

    PageableResponse<ProductResponse> getAllProductsOfCategory(Long id, String pageNumber, String pageSize, String sortBy, String sortDir);

}
