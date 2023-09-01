package com.api.note.services;

import com.api.note.config.system.service.DiskStorage;
import com.api.note.exceptions.DuplicatedException;
import com.api.note.exceptions.NotFoundException;
import com.api.note.model.User;
import com.api.note.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
            user.setId(id);
            return this.userRepository.save(user);
        }
        throw new NotFoundException("User not Found");
    }

    public void delete(Integer id) {
        Optional<User> user = this.userRepository.findById(id);
        user.ifPresent(this.userRepository::delete);
    }

    public User updateAvatar(Integer userId, MultipartFile file) {
      User user = this.findById(userId);
      if (user == null) throw new NotFoundException("User not found");
      DiskStorage disk = new DiskStorage();
      String filename = disk.saveFile(file);
      user.setAvatar(filename);
      return this.userRepository.save(user);
    }
}
