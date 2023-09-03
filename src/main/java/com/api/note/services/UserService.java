package com.api.note.services;

import com.api.note.config.service.DiskStorage;
import com.api.note.exceptions.DuplicatedException;
import com.api.note.exceptions.NotFoundException;
import com.api.note.model.User;
import com.api.note.model.enums.UserRole;
import com.api.note.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public List<User> findAll() {
        return Collections.unmodifiableList(this.userRepository.findAll());
    }

    public User findById(Integer id) {
        return this.userRepository.findAll().stream()
                        .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> { throw new NotFoundException("User not found"); });
    }

    public User findByEmail(String email) {
        return this.userRepository.findAll().stream()
                .filter(user -> user.getEmail() == email)
                .findFirst()
                .orElseThrow(() -> { throw new NotFoundException("User not found"); });
    }

    public User create(User user) {
        Optional<User> searchedUser = this.userRepository.findByEmail(user.getEmail());
        if (searchedUser.isEmpty()) {
            user.setPassword(encoder.encode(user.getPassword()));
            return this.userRepository.save(user);
        }
        throw new DuplicatedException("User already exists");
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User update(User user, Integer id) {
        Optional<User> searchedUser = this.userRepository.findById(id);
        if (searchedUser.isPresent()) {
            String encodedPassword = encoder.encode(user.getPassword());
            user.setId(id);
            user.setPassword(encodedPassword);
            if (searchedUser.get().getRole() != UserRole.ADMIN) {
                user.setRole(UserRole.USER);
            }
            return this.userRepository.save(user);
        }
        throw new NotFoundException("User not Found");
    }

    public void delete(Integer id) {
        Optional<User> user = this.userRepository.findById(id);
        user.ifPresent(this.userRepository::delete);
    }

    public User updateAvatar(Integer userId, MultipartFile file) throws IOException {
      User user = this.findById(userId);
      DiskStorage disk = new DiskStorage();
      if (user == null) throw new NotFoundException("User not found");
      if (user.getAvatar() != null) {
        if (!disk.deleteFile(user.getAvatar())) {
            
        }
      }
      String filename = disk.saveFile(file);
      user.setAvatar(filename);
      return this.userRepository.save(user);
    }

    public InputStream getAvatar(String filename) throws FileNotFoundException {
      DiskStorage disk = new DiskStorage();
      return disk.getFile(filename);
    }
}
