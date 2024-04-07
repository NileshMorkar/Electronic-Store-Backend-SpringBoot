package com.example.ElectronicStore.controller;


import com.example.ElectronicStore.dto.UserResponse;
import com.example.ElectronicStore.dto.jwt.JwtRequest;
import com.example.ElectronicStore.dto.jwt.JwtResponse;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.security.JwtHelper;
import com.example.ElectronicStore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Sign In Sign Up Controller", description = "This Is For Sign In Public API")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper helper;


    @Operation(summary = "Login To Page", description = "User For Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Call!!"),
            @ApiResponse(responseCode = "200", description = "All Is Well!!")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = helper.generateToken(userDetails);
        UserResponse userResponse = modelMapper.map(userDetails, UserResponse.class);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userResponse).build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new GlobalException("Invalid Username And Password !!", HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/user")
    public ResponseEntity<Map.Entry<String, String>> getCurrentUserName(Principal principal) {
        Map.Entry<String, String> m = Map.entry("User", principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(m);
    }

    @GetMapping
    public ResponseEntity<UserDetails> getCurrentUser(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.loadUserByUsername(principal.getName()));
    }


    //Exception Handling
    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }


}
