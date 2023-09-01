package com.api.note.config.system.service;

import org.springframework.web.multipart.MultipartFile;

import com.api.note.exceptions.NotFoundException;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDateTime;

@Slf4j
public class DiskStorage {


    public String saveFile(MultipartFile file) {
        
        if(! file.isEmpty()) {
            String filename = String.valueOf(this.hash(file.getOriginalFilename())) + "-" + file.getOriginalFilename();

            try {
                String uploadsDir = "src/main/resources/static/uploads/files/";
                if (! new File(uploadsDir).isDirectory()) {
                    new File(uploadsDir).mkdirs();
                }
                log.info("path upload = {}", uploadsDir);

                String fileToUpload = uploadsDir + filename;
                File dest = new File(fileToUpload);
                file.transferTo(dest);
                return filename;
            }
            catch (Exception e) {
                log.info("Error = {}", e.getMessage());
            }
        }

        throw new NotFoundException("File not found");

    }

    private Integer hash(String filename) {
        return filename.hashCode() * LocalDateTime.now().toString().hashCode() * 31;
    }
}
