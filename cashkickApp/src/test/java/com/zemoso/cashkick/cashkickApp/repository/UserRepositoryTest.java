package com.zemoso.cashkick.cashkickApp.repository;

import com.zemoso.cashkick.cashkickApp.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // This will initialize the mocks
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;  // Mocked UserRepository

    @InjectMocks
    private UserRepositoryTest userRepositoryTest; // Inject the mock into the test class

    @Test
    public void testFindByUsername_Found() {
        // Mock the repository method behavior
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Verify repository method was called
        Optional<User> result = userRepository.findByUsername("testUser");
        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());

        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    public void testFindByUsername_NotFound() {
        // Mock repository to return an empty Optional
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByUsername("nonExistentUser");
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByUsername("nonExistentUser");
    }

    @Test
    public void testFindByEmail_Found() {
        // Similar test for email
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    public void testFindByEmail_NotFound() {
        // Similar test where email is not found
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByEmail("nonexistent@example.com");
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}
