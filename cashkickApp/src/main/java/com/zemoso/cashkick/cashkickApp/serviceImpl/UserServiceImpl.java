package com.zemoso.cashkick.cashkickApp.serviceImpl;

import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
import com.zemoso.cashkick.cashkickApp.entities.User;
import com.zemoso.cashkick.cashkickApp.exception.ServiceException;
import com.zemoso.cashkick.cashkickApp.exception.UserAlreadyExistException;
import com.zemoso.cashkick.cashkickApp.repository.UserRepository;
import com.zemoso.cashkick.cashkickApp.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(UserDTO userDTO) {
        // Validate the username here, instead of in the controller
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            LOGGER.error("Username is required");
            throw new IllegalArgumentException("Username is required");
        }

        // Check if the username or email already exists
        Optional<User> existingUserByUserName = userRepository.findByUsername(userDTO.getUsername());
        if (existingUserByUserName.isPresent()) {
            LOGGER.error("User with username {} already exists", userDTO.getUsername());
            throw new UserAlreadyExistException("User Name already exists");
        }

        Optional<User> existingUserByEmail = userRepository.findByEmail(userDTO.getEmail());
        if (existingUserByEmail.isPresent()) {
            LOGGER.error("User with email {} already exists", userDTO.getEmail());
            throw new UserAlreadyExistException("Email already exists");
        }

        // If validation passed, map UserDTO to User entity and save it
        User user = modelMapper.map(userDTO, User.class);
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while saving user", e);
            throw new ServiceException("Some unexpected error occurred");
        }
    }
}

