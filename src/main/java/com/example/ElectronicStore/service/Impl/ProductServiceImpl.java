package com.example.ElectronicStore.service.Impl;

import com.example.ElectronicStore.Helper.Helper;
import com.example.ElectronicStore.dto.PageableResponse;
import com.example.ElectronicStore.dto.ProductRequest;
import com.example.ElectronicStore.dto.ProductResponse;
import com.example.ElectronicStore.entity.CategoryEntity;
import com.example.ElectronicStore.entity.ProductEntity;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.repository.CategoryRepository;
import com.example.ElectronicStore.repository.ProductRepository;
import com.example.ElectronicStore.service.ProductService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Date;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ProductResponse create(ProductRequest productRequest) {

        ProductEntity productEntity = modelMapper.map(productRequest, ProductEntity.class);
        productEntity.setDate(new Date());
        log.info(" ==========> {}", productRequest);
        log.info(" ==========> {}", productEntity);
        productRepository.save(productEntity);
        return modelMapper.map(productEntity, ProductResponse.class);
    }

    @Override
    public ProductResponse getById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new GlobalException("Product Id Is Not Present!", HttpStatus.OK));
        return modelMapper.map(productEntity, ProductResponse.class);
    }

    @Override
    public ResponseEntity<PageableResponse<ProductResponse>> getAll(String pageNumber, String pageSize, String sortBy, String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("acs") ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
            PageRequest pageRequest = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), sort);
            Page<ProductEntity> page = productRepository.findAll(pageRequest);

            PageableResponse<ProductResponse> response = Helper.getPageableResponse(page, ProductResponse.class);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<PageableResponse<ProductResponse>> getAllLive(String pageNumber, String pageSize, String sortBy, String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("acs") ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
            PageRequest pageRequest = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), sort);
            Page<ProductEntity> page = productRepository.findByIsLiveTrue(pageRequest);

            PageableResponse<ProductResponse> response = Helper.getPageableResponse(page, ProductResponse.class);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void update(Long id, ProductRequest productRequest) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new GlobalException("Product Id Is Not Present!", HttpStatus.OK));

        productEntity.setName(productRequest.getName());
        productEntity.setDescription(productRequest.getDescription());
        productEntity.setLive(productRequest.isLive());
        productEntity.setInStock(productRequest.isInStock());
        productEntity.setDiscount(productRequest.getDiscount());
        productEntity.setPrice(productRequest.getPrice());
        productEntity.setQuantity(productRequest.getQuantity());
        productEntity.setImage(productRequest.getImage());

        productEntity.setCategory(modelMapper.map(productRequest.getCategory(), CategoryEntity.class));

        productRepository.save(productEntity);
    }

    @Override
    public void delete(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new GlobalException("Product Id Is Not Present!", HttpStatus.OK));

        //Delete Image
        try {
            Path path = Paths.get(productEntity.getImage());
            Files.delete(path);
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        productRepository.delete(productEntity);
    }

    @Override
    public ResponseEntity<PageableResponse<ProductResponse>> searchByName(String name, String pageNumber, String pageSize, String sortBy, String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("acs") ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
            PageRequest pageRequest = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), sort);
            Page<ProductEntity> page = productRepository.findByNameContaining(name, pageRequest);

            PageableResponse<ProductResponse> response = Helper.getPageableResponse(page, ProductResponse.class);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PageableResponse<ProductResponse> getAllProductsOfCategory(Long id, String pageNumber, String pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new GlobalException("Category Id Not Present!", HttpStatus.OK));
        PageRequest pageRequest = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), sort);

        Page<ProductEntity> page = productRepository.findByCategory(categoryEntity, pageRequest);

        return Helper.getPageableResponse(page, ProductResponse.class);
    }


}
