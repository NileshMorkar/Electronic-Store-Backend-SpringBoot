package com.example.ElectronicStore.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {

    String uploadFile(MultipartFile file, String path, Long id);

    InputStream getFile(String fullPath);
}
