package com.demo.user.service.services;

import com.demo.user.service.dao.UserRepository;
import com.demo.user.service.exceptions.UserAlreadyExistsException;
import com.demo.user.service.exceptions.UserNotFoundException;
import com.demo.user.service.model.BlogUser;
import com.demo.user.service.payload.BlogUserDTO;
import com.demo.user.service.security.JwtResponse;
import com.demo.user.service.security.JwtTokenProvider;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public BlogUserDTO registerUser(BlogUserDTO userDTO) {
        BlogUser existingUser = userRepository.findByUsername(userDTO.getUsername());

        if (existingUser != null) {
            throw new UserAlreadyExistsException("Username '" + userDTO.getUsername() + "' is already taken.");
        }
        // Check if email is already registered
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email '" + userDTO.getEmail() + "' is already registered.");
        }

        BlogUser user = modelMapper.map(userDTO, BlogUser.class);
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        BlogUser savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, BlogUserDTO.class);
    }

    @Override
    public JwtResponse authenticateUser(String username, String password) {
        BlogUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new UserNotFoundException("Invalid password.");
        }
        String token = jwtTokenProvider.generateToken(user);
        return new JwtResponse(token);
    }

    @Override
    public Long getDetailsFromToken(String token) {
        boolean isValid = jwtTokenProvider.validateToken(token);
        if (!isValid) {
            throw new UserNotFoundException("Invalid or expired token.");
        }
        Long id;
        try {
            id = jwtTokenProvider.getUserIdFromJWT(token);
        } catch (Exception ex) {
            throw new UserNotFoundException("Malformed or invalid token.");
        }
        return id;
    }

    @Override
    public BlogUserDTO getBlogUserById(Long userId) {
        BlogUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return modelMapper.map(user, BlogUserDTO.class);
    }

    @Override
    public List<BlogUserDTO> getAllBlogUsers() {
        List<BlogUser> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, BlogUserDTO.class))
                .collect(Collectors.toList());
    }
}
