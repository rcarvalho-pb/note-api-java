package com.api.note.config.auth;

import com.api.note.model.User;
import com.api.note.model.dto.UserDTO;
import com.api.note.model.enums.UserRole;
import com.api.note.repositories.UserRepository;
import com.api.note.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "auth")
@RequiredArgsConstructor
@Log
public class AuthenticationRestController {

    private final UserRepository userJpaRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;
    private final UserService service;

    @PostMapping(value = "login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        var authentication = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(authentication);
        User user = userJpaRepository.findByEmail(request.email()).orElseThrow();
        String token = jwtService.createToken(user);
        return new AuthenticationResponse(user.getId(), token);
    }

    @PostMapping(value = "register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserDTO data) {
        String encryptedPassword = encoder.encode(data.password());
        User user = new User(data.name(), data.email(), encryptedPassword, UserRole.USER);
        service.save(user);
    }
}
