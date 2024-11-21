package com.zemoso.cashkick.cashkickApp.service;

import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
import com.zemoso.cashkick.cashkickApp.entities.User;

public interface UserService {
    User registerUser(UserDTO userDTO);


}
