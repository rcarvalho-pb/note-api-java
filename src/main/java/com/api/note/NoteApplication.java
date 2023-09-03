package com.api.note;

import com.api.note.model.User;
import com.api.note.model.enums.UserRole;
import com.api.note.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class NoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepository userRepository, PasswordEncoder encoder) {
		return args -> {
			if (userRepository.findByEmail("admin").isPresent()) return;
			User admin = User.builder()
					.email("admin")
					.password(encoder.encode("123"))
					.role(UserRole.ADMIN)
					.build();
			userRepository.save(admin);
		};
	}

}
