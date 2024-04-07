package com.example.ElectronicStore.controller;

import com.example.ElectronicStore.dto.*;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.service.CategoryService;
import com.example.ElectronicStore.service.FileService;
import com.example.ElectronicStore.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Autowired
    ProductService productService;

    @Value("${category.image.path}")
    private String uploadImagePath;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryRequest categoryRequest) {
        return categoryService.create(categoryRequest);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) throws GlobalException {
        CategoryResponse categoryResponse = categoryService.getById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryResponse> getByName(@PathVariable String name) {
        return categoryService.getByName(name);
    }

    @DeleteMapping("/id/{id}")
    ResponseEntity<ApiResponseMessage> delete(@PathVariable Long id) throws GlobalException {
        return categoryService.delete(id);
    }


    @GetMapping
    ResponseEntity<PageableResponse<CategoryResponse>> getAll(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) String pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize,
            @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
    }

    @PutMapping("/id/{id}")
    ResponseEntity<ApiResponseMessage> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest newCategory) throws GlobalException {
        return categoryService.update(id, newCategory);
    }


    @PostMapping("/image/{id}")
    ResponseEntity<ApiResponseMessage> uploadImage(@PathVariable Long id, @RequestParam(value = "categoryImage") MultipartFile file) {


        CategoryResponse categoryResponse = categoryService.getById(id);

        String imageFullPath = fileService.uploadFile(file, uploadImagePath, id);

        assert categoryResponse != null;
        categoryResponse.setImage(imageFullPath);

        CategoryRequest categoryRequest = new CategoryRequest();
        BeanUtils.copyProperties(categoryResponse, categoryRequest);

        categoryService.update(id, categoryRequest);

        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .httpStatus(HttpStatus.CREATED)
                .message("Image Upload Successfully!")
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @GetMapping("/image/{id}")
    public void getImage(@PathVariable Long id, HttpServletResponse response) {

        CategoryResponse categoryResponse = categoryService.getById(id);

        assert categoryResponse != null;
        if (categoryResponse.getImage() == null)
            throw new GlobalException("Image Not Present!", HttpStatus.OK);

        InputStream inputStream = fileService.getFile(categoryResponse.getImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        try {
            StreamUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //create product with category
    @PostMapping("/{categoryId}/product")
    public ResponseEntity<ProductResponse> createProductForCategory(@PathVariable Long categoryId, @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = categoryService.createProductForCategory(categoryId, productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }


    //Assign Category To Product
    @PutMapping("/{categoryId}/update-product/{productId}")
    public ResponseEntity<ProductResponse> updateCategoryOfProduct(@PathVariable Long categoryId, @PathVariable Long productId) {

        ProductResponse productResponse = categoryService.updateCategoryOfProduct(categoryId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }


    @GetMapping("/id/{id}/products")
    public ResponseEntity<PageableResponse<ProductResponse>> getAllProductsOfCategory(@PathVariable Long id
            , @RequestParam(name = "pageNumber", defaultValue = "0", required = false) String pageNumber
            , @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize
            , @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy
            , @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductResponse> pageableResponse = productService.getAllProductsOfCategory(id, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(pageableResponse);
    }

}
