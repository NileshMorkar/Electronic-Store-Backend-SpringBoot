package com.example.ElectronicStore.service.Impl;

import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileServiceImpl implements FileService {


    @Override
    public String uploadFile(MultipartFile file, String path, Long id) {

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf('.'));

        String fileNameWithExtension = id + extension;

        String fullPathWithFileName = path + fileNameWithExtension;

        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase("jpeg")) {

            File folder = new File(path);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            try {
                Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            } catch (IOException e) {
                throw new GlobalException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            log.info("\n===========>{},{}\n", fileNameWithExtension, fullPathWithFileName);
            return fullPathWithFileName;
        } else {
            throw new GlobalException("File " + extension + " Extension Not Supported!", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public InputStream getFile(String fullPath) {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            throw new GlobalException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return inputStream;
    }
}
