package com.example.ElectronicStore.controller;


import com.example.ElectronicStore.dto.PageableResponse;
import com.example.ElectronicStore.dto.ProductResponse;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.service.FileService;
import com.example.ElectronicStore.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    FileService fileService;

    @Value("${product.image.path}")
    String imageUploadPath;


    @GetMapping("/id/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) throws GlobalException {
        ProductResponse productResponse = productService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<PageableResponse<ProductResponse>> searchByName(@PathVariable String name
            , @RequestParam(name = "pageNumber", defaultValue = "0", required = false) String pageNumber
            , @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize
            , @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy
            , @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return productService.searchByName(name, pageNumber, pageSize, sortBy, sortDir);
    }


    @Tag(name = "Get All Products")
    @GetMapping
    ResponseEntity<PageableResponse<ProductResponse>> getAll(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) String pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize,
            @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return productService.getAll(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/live")
    ResponseEntity<PageableResponse<ProductResponse>> getAllLive(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) String pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize,
            @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
    }


    @GetMapping("/image/{id}")
    public void getImage(@PathVariable Long id, HttpServletResponse response) {

        ProductResponse product = productService.getById(id);

        if (product.getImage() == null)
            throw new GlobalException("Image Not Present!", HttpStatus.OK);

        InputStream inputStream = fileService.getFile(product.getImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        try {
            StreamUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
