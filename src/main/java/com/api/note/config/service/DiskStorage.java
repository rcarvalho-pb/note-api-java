package com.api.note.config.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.api.note.exceptions.NotFoundException;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public class DiskStorage {

//    @Value("${data.name}")
    private String uploadsDir = "./src/main/resources/static/images/";


    public String saveFile(MultipartFile file) {
        
        if(! file.isEmpty()) {
            String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            try {
                if (! new File(uploadsDir).isDirectory()) {
                    Files.createDirectories(Paths.get(uploadsDir));
                }

                Files.copy(file.getInputStream(), Paths.get(uploadsDir, filename));
                return filename;
            }
            catch (Exception e) {
                log.info("Error = {}", e.getMessage());
            }
        }

        throw new NotFoundException("File not found");

    }

    public InputStream getFile(String filename) throws FileNotFoundException {
        String path = uploadsDir + filename;
        InputStream file = new FileInputStream(path);
        return file;
    }

    public boolean deleteFile(String avatar) {
        try {
            Files.delete(Paths.get(uploadsDir, avatar));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
