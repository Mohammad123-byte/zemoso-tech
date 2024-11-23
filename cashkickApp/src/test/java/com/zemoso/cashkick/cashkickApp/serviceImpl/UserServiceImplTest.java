package com.zemoso.cashkick.cashkickApp.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
import com.zemoso.cashkick.cashkickApp.entities.User;
import com.zemoso.cashkick.cashkickApp.exception.ServiceException;
import com.zemoso.cashkick.cashkickApp.exception.UserAlreadyExistException;
import com.zemoso.cashkick.cashkickApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    // Utility method to create a UserDTO from JSON
    private UserDTO createUserDTOFromJson(String userJson) throws JsonProcessingException {
        return objectMapper.readValue(userJson, UserDTO.class);
    }

    @Test
    public void testRegisterUser_Success() throws JsonProcessingException {
        // Prepare the userDTO string and expected User object
        String userDTOJson = "{\"username\":\"testUser\", \"email\":\"test@example.com\"}";
        UserDTO userDTO = createUserDTOFromJson(userDTOJson);

        // Convert to User entity
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Mock repository behavior
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Test the service method
        User savedUser = userService.registerUser(userDTO);

        // Validate the result
        assertNotNull(savedUser);
        assertEquals("testUser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());

        // Verify interactions with the repository
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testRegisterUser_UsernameNull() throws JsonProcessingException {
        // Prepare the userDTO string with null username
        String userDTOJson = "{\"username\":null, \"email\":\"test@example.com\"}";
        UserDTO userDTO = createUserDTOFromJson(userDTOJson);

        // Test the service method and assert exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userDTO));
        assertEquals("Username is required", exception.getMessage());

        // Verify repository interaction
        verify(userRepository, times(0)).findByUsername(anyString());
        verify(userRepository, times(0)).findByEmail(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameEmpty() throws JsonProcessingException {
        // Prepare the userDTO string with empty username
        String userDTOJson = "{\"username\":\"  \", \"email\":\"test@example.com\"}";
        UserDTO userDTO = createUserDTOFromJson(userDTOJson);

        // Test the service method and assert exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userDTO));
        assertEquals("Username is required", exception.getMessage());

        // Verify repository interaction
        verify(userRepository, times(0)).findByUsername(anyString());
        verify(userRepository, times(0)).findByEmail(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }


    @Test
    public void testRegisterUser_UserAlreadyExists() throws JsonProcessingException {
        // Prepare the userDTO string
        String userDTOJson = "{\"username\":\"testUser\", \"email\":\"test@example.com\"}";
        UserDTO userDTO = createUserDTOFromJson(userDTOJson);

        // Convert to User entity
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Mock repository behavior
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));

        // Test the service method and assert exception
        assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(userDTO));

        // Verify repository interaction
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
        verify(userRepository, times(0)).findByEmail(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() throws JsonProcessingException {
        // Prepare the userDTO string
        String userDTOJson = "{\"username\":\"testUser\", \"email\":\"test@example.com\"}";
        UserDTO userDTO = createUserDTOFromJson(userDTOJson);

        // Convert to User entity
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Mock repository behavior
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        // Test the service method and assert exception
        assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(userDTO));

        // Verify repository interaction
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, times(0)).save(any(User.class));
    }

  /*  @Test
    public void testRegisterUser_ServiceException() throws JsonProcessingException {
        // Prepare the userDTO string
        String userDTOJson = "{\"username\":\"testUser\", \"email\":\"test@example.com\"}";
        UserDTO userDTO = createUserDTOFromJson(userDTOJson);

        // Convert to User entity
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Mock repository behavior
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        // Simulate unexpected error when saving the user (e.g., a database failure)
        when(userRepository.save(any(User.class))).thenThrow(new ServiceException("Some unexpected error occurred"));

        // Test the service method and assert that ServiceException is thrown
        assertThrows(ServiceException.class, () -> userService.registerUser(userDTO));

        // Verify repository interaction
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }*/

    @Test
    public void testRegisterUser_ServiceException() throws JsonProcessingException {
        // Prepare the userDTO string
        String userDTOJson = "{\"username\":\"testUser\", \"email\":\"test@example.com\"}";
        UserDTO userDTO = createUserDTOFromJson(userDTOJson);

        // Convert to User entity
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Mock repository behavior
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        // Simulate an unexpected error when saving the user (e.g., a database failure)
        when(userRepository.save(any(User.class))).thenThrow(new ServiceException("Some unexpected error occurred"));

        // Test the service method and assert that ServiceException is thrown
        ServiceException exception = assertThrows(ServiceException.class, () -> userService.registerUser(userDTO));
        assertEquals("Some unexpected error occurred", exception.getMessage());

        // Verify repository interaction
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    public void testRegisterUser_InvalidJson() {
        // Invalid JSON that cannot be deserialized
        String invalidUserDTOJson = "{\"username\":\"testUser\", \"email\":\"test@example.com\"";  // Missing closing brace

        // Since the registerUser method takes a UserDTO, we need to handle invalid JSON
        // before calling registerUser, as the UserDTO is required.

        // Attempt to deserialize the invalid JSON, and expect an exception
        assertThrows(JsonProcessingException.class, () -> {
            // Try to convert the invalid JSON string to UserDTO
            UserDTO userDTO = objectMapper.readValue(invalidUserDTOJson, UserDTO.class);

            // If deserialization works (which it shouldn't), call registerUser
            userService.registerUser(userDTO);
        });
    }

}
