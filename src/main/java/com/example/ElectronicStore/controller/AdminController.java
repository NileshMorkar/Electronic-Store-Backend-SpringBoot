package com.example.ElectronicStore.controller;


import com.example.ElectronicStore.dto.*;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.service.FileService;
import com.example.ElectronicStore.service.ProductService;
import com.example.ElectronicStore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @Value("${product.image.path}")
    String imageUploadPath;

    @PostMapping("/products/create-product")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        ProductResponse productResponse = productService.create(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/products/id/{id}")
    ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable Long id) throws GlobalException {
        productService.delete(id);
        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .message("Product Deleted Successfully!")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/products/id/{id}")
    ResponseEntity<ApiResponseMessage> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        productService.update(id, productRequest);
        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .message("Product Updated Successfully!")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PostMapping("/products/image/{id}")
    ResponseEntity<ApiResponseMessage> uploadImage(@PathVariable Long id
            , @RequestParam("productImage") MultipartFile file
    ) {

        ProductResponse product = productService.getById(id);

        String fullImagePath = fileService.uploadFile(file, imageUploadPath, id);

        product.setImage(fullImagePath);

        ProductRequest productRequest = new ProductRequest();
        BeanUtils.copyProperties(product, productRequest);

        productService.update(id, productRequest);

        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .success(true)
                .message("Image Uploaded Successfully!")
                .httpStatus(HttpStatus.CREATED)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }


    @GetMapping("/user/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws GlobalException {

        UserResponse userResponse = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);

    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) throws GlobalException {
        return userService.getByEmail(email);
    }


    @GetMapping("/user/name/{name}")
    public ResponseEntity<List<UserResponse>> searchUserByName(@PathVariable String name) throws GlobalException {
        return userService.search(name);
    }

    @GetMapping
    ResponseEntity<PageableResponse<UserResponse>> getAllUsers(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) String pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize,
            @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return userService.getAll(pageNumber, pageSize, sortBy, sortDir);
    }

}
