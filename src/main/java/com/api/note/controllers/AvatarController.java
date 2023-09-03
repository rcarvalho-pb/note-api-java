package com.api.note.controllers;

import com.api.note.model.User;
import com.api.note.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(value = "avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final UserService userService;

    @PostMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public User updateAvatar(@PathVariable(name = "userId") Integer userId, @RequestBody MultipartFile file) throws IOException {
        return this.userService.updateAvatar(userId, file);
    }

    @GetMapping(value = "/{filename}", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void getAvatar(@PathVariable String filename, HttpServletResponse response) throws IOException {
        InputStream file = this.userService.getAvatar(filename);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(file, response.getOutputStream());
    }
}
