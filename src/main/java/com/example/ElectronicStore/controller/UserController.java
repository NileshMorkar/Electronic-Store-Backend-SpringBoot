package com.example.ElectronicStore.controller;


import com.example.ElectronicStore.dto.ApiResponseMessage;
import com.example.ElectronicStore.dto.UserRequest;
import com.example.ElectronicStore.dto.UserResponse;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.service.FileService;
import com.example.ElectronicStore.service.UserService;
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

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @Value("${user.profile.image.path}")
    String imageUploadPath;

    @PostMapping("/create-user")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest userRequest) {
        return userService.createNormalUser(userRequest);
    }


    @DeleteMapping("/id/{id}")
    ResponseEntity<ApiResponseMessage> delete(@PathVariable Long id) throws GlobalException {
        return userService.delete(id);
    }

    @PutMapping("/id/{id}")
    ResponseEntity<ApiResponseMessage> update(@PathVariable Long id, @Valid @RequestBody UserRequest newUser) throws GlobalException {
        return userService.update(id, newUser);
    }

    @PostMapping("image/{id}")
    public ResponseEntity<ApiResponseMessage> uploadImage(@PathVariable Long id, @RequestParam("userImage") MultipartFile file) {

        UserResponse userResponse = userService.getById(id);

        String fullImagePath = fileService.uploadFile(file, imageUploadPath, id);
        assert userResponse != null;
        userResponse.setImage(fullImagePath);

        UserRequest userRequest = new UserRequest();
        BeanUtils.copyProperties(userResponse, userRequest);
        userService.update(id, userRequest);

        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("File Upload Successfully")
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/image/{id}")
    public void getImage(@PathVariable Long id, HttpServletResponse response) {

        UserResponse userResponse = userService.getById(id);

        assert userResponse != null;
        if (userResponse.getImage() == null)
            throw new GlobalException("Image Not Present!", HttpStatus.OK);

        InputStream inputStream = fileService.getFile(userResponse.getImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        try {
            StreamUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new GlobalException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
