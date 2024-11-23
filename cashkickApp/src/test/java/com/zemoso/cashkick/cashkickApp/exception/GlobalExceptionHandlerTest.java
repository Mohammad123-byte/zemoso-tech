package com.zemoso.cashkick.cashkickApp.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // Add this annotation to enable Mockito in JUnit 5
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;  // This should now be injected correctly

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;// Mock BindingResult


    @Test
    void testHandleValidationExceptions() {
        // Step 1: Set up the mock FieldError
        FieldError fieldError = new FieldError(
                "someObject",  // object name
                "someField",   // field name
                "This field is required"
        );
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        // Step 2: Set up the exception object
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // Step 3: Call the handler method
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleValidationExceptions(exception);

        // Step 4: Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // Check the error response body
        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
        assertEquals("Validation failed", errorResponse.getMessage());
        assertTrue(errorResponse.getFieldErrors().containsKey("someField"));
        assertEquals("This field is required", errorResponse.getFieldErrors().get("someField"));
    }

    @Test
    public void testHandleDuplicateEntryException() {
        // Create mock DuplicateEntryException
        DuplicateEntryException exception = new DuplicateEntryException("Duplicate user entry");

        // Call the method
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDuplicateEntryException(exception);

        // Verify the response
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getStatusCode());
        assertEquals("Duplicate user entry", errorResponse.getMessage());
    }

    @Test
    public void testHandleServiceException() {
        // Create mock ServiceException
        ServiceException exception = new ServiceException("Service error occurred");

        // Call the method
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleServiceException(exception);

        // Verify the response
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatusCode());
        assertEquals("Service error occurred", errorResponse.getMessage());
    }

    @Test
    public void testHandleGeneralException() {
        // Create mock generic Exception
        Exception exception = new Exception("Unexpected error");

        // Call the method
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneralException(exception);

        // Verify the response
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatusCode());
        assertEquals("Un Expected error occurred", errorResponse.getMessage());
    }

    @Test
    public void testHandleUserAlreadyExistException() {
        // Create mock UserAlreadyExistException
        UserAlreadyExistException exception = new UserAlreadyExistException("User already exists");

        // Call the method
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserAlreadyExistException(exception);

        // Verify the response
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getStatusCode());
        assertEquals("User already exists", errorResponse.getMessage());
    }

    @Test
    public void testHandleUserNotFoundException() {
        // Create mock UserNotFoundException
        UserNotFoundException exception = new UserNotFoundException("User not found");

        // Call the method
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNotFoundException(exception);

        // Verify the response
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatusCode());
        assertEquals("User not found", errorResponse.getMessage());
    }
}
