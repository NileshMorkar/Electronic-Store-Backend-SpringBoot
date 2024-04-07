package com.example.ElectronicStore.service.Impl;

import com.example.ElectronicStore.Helper.Helper;
import com.example.ElectronicStore.dto.*;
import com.example.ElectronicStore.entity.CategoryEntity;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.repository.CategoryRepository;
import com.example.ElectronicStore.service.CategoryService;
import com.example.ElectronicStore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<CategoryResponse> create(CategoryRequest categoryRequest) {

        try {
            Optional<CategoryEntity> entity = categoryRepository.findByName(categoryRequest.getName());

            if (entity.isPresent()) {
                throw new GlobalException("Category Is Already Present!", HttpStatus.OK);
            }

            CategoryEntity categoryEntity = modelMapper.map(categoryRequest, CategoryEntity.class);
            categoryRepository.save(categoryEntity);
            CategoryResponse categoryResponse = modelMapper.map(categoryEntity, CategoryResponse.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);

        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CategoryResponse getById(Long id) {

        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new GlobalException("Category Id Not Present!", HttpStatus.OK));
        return modelMapper.map(categoryEntity, CategoryResponse.class);
    }

    @Override
    public ResponseEntity<CategoryResponse> getByName(String name) {

        CategoryEntity categoryEntity = categoryRepository.findByName(name).orElseThrow(() -> new GlobalException("Category Id Not Present!", HttpStatus.OK));
        CategoryResponse categoryResponse = modelMapper.map(categoryEntity, CategoryResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    @Override
    public ResponseEntity<PageableResponse<CategoryResponse>> getAll(String pageNumber, String pageSize, String sortBy, String sortDir) {

        try {
            Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
            PageRequest pageRequest = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), sort);
            Page<CategoryEntity> page = categoryRepository.findAll(pageRequest);
            PageableResponse<CategoryResponse> pageableResponse = Helper.getPageableResponse(page, CategoryResponse.class);

            return ResponseEntity.status(HttpStatus.OK).body(pageableResponse);
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponseMessage> update(Long id, CategoryRequest newCategory) {

        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new GlobalException("Category Id Not Present!", HttpStatus.OK));

        categoryEntity.setDescription(newCategory.getDescription());
        categoryEntity.setImage(newCategory.getImage());
        categoryEntity.setName(newCategory.getName());

        categoryRepository.save(categoryEntity);

        ApiResponseMessage message = ApiResponseMessage
                .builder()
                .message("Category Updated Successfully!")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Override
    public ResponseEntity<ApiResponseMessage> delete(Long id) {

        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new GlobalException("Category Id Not Present!", HttpStatus.OK));

        Path path = Paths.get(categoryEntity.getImage());
        try {
            Files.delete(path);
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        categoryRepository.delete(categoryEntity);

        ApiResponseMessage message = ApiResponseMessage
                .builder()
                .message("Category Deleted Successfully!")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Override
    public ProductResponse createProductForCategory(Long categoryId, ProductRequest productRequest) {

        CategoryResponse categoryResponse = getById(categoryId);

        productRequest.setCategory(categoryResponse);

        return productService.create(productRequest);
    }

    @Override
    public ProductResponse updateCategoryOfProduct(Long categoryId, Long productId) {

        ProductResponse productResponse = productService.getById(productId);
        CategoryResponse categoryResponse = getById(categoryId);

        productResponse.setCategory(categoryResponse);

        ProductRequest productRequest = modelMapper.map(productResponse, ProductRequest.class);
        productService.update(productId, productRequest);

        return productResponse;
    }


}
