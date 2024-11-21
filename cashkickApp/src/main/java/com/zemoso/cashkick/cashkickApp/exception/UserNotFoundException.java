package com.zemoso.cashkick.cashkickApp.exception;

import org.springframework.data.jpa.repository.JpaRepository;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}
