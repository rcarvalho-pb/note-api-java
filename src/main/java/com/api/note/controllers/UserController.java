package com.api.note.controllers;

import com.api.note.model.User;
import com.api.note.services.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return this.userService.findAll();
    }

    @GetMapping(value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findById(@PathVariable(value = "id") Integer id) {
        return this.userService.findById(id);
    }

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public User findByEmail(@RequestParam(value = "email") String email) {
        return this.userService.findByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return this.userService.create(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('USER')")
    public User update(@RequestBody User user, @PathVariable Integer id) {
        return this.userService.update(user, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id) {
        this.userService.delete(id);
    }



}
