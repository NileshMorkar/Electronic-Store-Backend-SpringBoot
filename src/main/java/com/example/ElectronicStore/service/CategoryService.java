package com.example.ElectronicStore.service;

import com.example.ElectronicStore.dto.*;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

    ResponseEntity<CategoryResponse> create(CategoryRequest categoryRequest);

    CategoryResponse getById(Long id);

    ResponseEntity<CategoryResponse> getByName(String name);

    ResponseEntity<PageableResponse<CategoryResponse>> getAll(String pageNumber, String pageSize, String sortBy, String sortDir);

    ResponseEntity<ApiResponseMessage> update(Long id, CategoryRequest newCategory);

    ResponseEntity<ApiResponseMessage> delete(Long id);

    ProductResponse createProductForCategory(Long categoryId, ProductRequest productRequest);

    ProductResponse updateCategoryOfProduct(Long categoryId, Long productId);


}
