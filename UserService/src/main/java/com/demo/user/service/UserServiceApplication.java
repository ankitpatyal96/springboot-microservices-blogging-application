package com.demo.user.service;

import com.demo.user.service.dao.UserRepository;
import com.demo.user.service.model.BlogUser;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper()
;	}

	@Bean
	public CommandLineRunner dummyDataLoader(UserRepository userRepository) {
		return args -> {
			// Add dummy users only if the users table is empty
			if (userRepository.count() == 0) {
				insertDummyUsers(userRepository);
			}
		};
	}

	private void insertDummyUsers(UserRepository userRepository) {
		BlogUser user1 = new BlogUser();
		user1.setUsername("user1");
		user1.setEmail("user1@example.com");
		user1.setPassword(BCrypt.hashpw("password1", BCrypt.gensalt()));

		BlogUser user2 = new BlogUser();
		user2.setUsername("user2");
		user2.setEmail("user2@example.com");
		user2.setPassword(BCrypt.hashpw("password2", BCrypt.gensalt()));

		userRepository.save(user1);
		userRepository.save(user2);
	}

}
