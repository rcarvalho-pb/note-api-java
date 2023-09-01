package com.api.note.config.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class DiskStorage {

    private Path rootSaveFolder = Paths.get("src", "main", "resource", "static", "image");

    public String saveFile(MultipartFile file) {
        String filename = String.valueOf(this.hash(file.getOriginalFilename())) + " - " + file.getOriginalFilename();
        if (!file.isEmpty()) {
            Path arquivoPath =
                    rootSaveFolder.resolve(filename);
            try {
                if (!Files.isDirectory(rootSaveFolder)) {
                    Files.createDirectory(rootSaveFolder);
                }
                file.transferTo(rootSaveFolder);
                File avatar = new File(rootSaveFolder.toUri()+file.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Integer hash(String filename) {
        return filename.hashCode() * LocalDateTime.now().toString().hashCode() * 31;
    }
}
