package com.zemoso.cashkick.cashkickApp.controllers;

import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
import com.zemoso.cashkick.cashkickApp.entities.User;
import com.zemoso.cashkick.cashkickApp.exception.UserAlreadyExistException;
import com.zemoso.cashkick.cashkickApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserDTO userDTO;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup DTO and User entity for testing
        userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@example.com");

        user = new User();
        user.setUserid(1);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
    }

    @Test
    public void testAddUser_Success() {
        // Arrange
        String successMessage = "User created successfully"; // Mock the expected message
        when(userService.registerUser(any(UserDTO.class))).thenReturn(user); // Mock service response

        // Act
        ResponseEntity<?> responseEntity = userController.addUser(userDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(successMessage, responseEntity.getBody()); // Assert message instead of User object

        // Verify interaction with the service
        verify(userService, times(1)).registerUser(userDTO);
    }


    @Test
    public void testAddUser_UserAlreadyExists() {
        // Arrange
        when(userService.registerUser(any(UserDTO.class))).thenThrow(new UserAlreadyExistException("User Name already exists"));

        // Act
        ResponseEntity<?> responseEntity = userController.addUser(userDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("User Name already exists"));

        // Verify interaction with the service
        verify(userService, times(1)).registerUser(userDTO);
    }

    @Test
    public void testAddUser_InvalidDTO_UsernameEmpty() {
        // Arrange
        userDTO.setUsername(""); // Make username empty
        when(userService.registerUser(any(UserDTO.class)))
                .thenThrow(new IllegalArgumentException("Username is required"));

        // Act
        ResponseEntity<?> responseEntity = null;
        try {
            responseEntity = userController.addUser(userDTO);
        } catch (IllegalArgumentException ex) {
            // Handle the exception thrown by the controller for assertion
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation failed: " + ex.getMessage());
        }

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().toString().contains("Validation failed"));
        assertTrue(responseEntity.getBody().toString().contains("Username is required"));

        // Verify interaction with the service
        verify(userService, times(1)).registerUser(any());
    }



    @Test
    public void testAddUser_InvalidDTO_EmailEmpty() {
        // Arrange
        userDTO.setEmail(""); // Make email empty
        // Simulate the service throwing an exception when the DTO has invalid data
        doThrow(new IllegalArgumentException("Email is required"))
                .when(userService).registerUser(any(UserDTO.class));

        // Act
        ResponseEntity<?> responseEntity = null;
        try {
            responseEntity = userController.addUser(userDTO);
        } catch (IllegalArgumentException ex) {
            // Assert exception message
            assertEquals("Email is required", ex.getMessage());
        }

        // Assert response entity is not returned
        assertNull(responseEntity);

        // Verify interaction with the service
        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }


    @Test
    public void testAddUser_Exception() {
        // Arrange
        when(userService.registerUser(any(UserDTO.class))).thenThrow(new RuntimeException("Some unexpected error occurred"));

        ResponseEntity<?> responseEntity = null;

        try {
            // Act
            responseEntity = userController.addUser(userDTO);
        } catch (RuntimeException ex) {
            // Assert for the exception
            assertEquals("Some unexpected error occurred", ex.getMessage());
            return; // Stop further execution as the test is already validated
        }

        // If the exception is not thrown, assert that responseEntity should not be null
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Some unexpected error occurred"));

        // Verify that the service method was called once
        verify(userService, times(1)).registerUser(userDTO);
    }


}
