package com.zemoso.cashkick.cashkickApp.mapper;

import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
import com.zemoso.cashkick.cashkickApp.entities.User;
import org.modelmapper.ModelMapper;

public class UserMapper {

    private static ModelMapper modelMapper = new ModelMapper();

    static {
        // Custom configurations if needed
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    // Convert UserDTO to User entity
    public static User convertToEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return modelMapper.map(userDTO, User.class);
    }

    // Convert User entity to UserDTO
    public static UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }
        return modelMapper.map(user, UserDTO.class);
    }
}
