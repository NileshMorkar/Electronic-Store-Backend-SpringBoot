package com.example.ElectronicStore.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/health")
    public String healthCheck() {
        return "OK !!!";
    }

}
