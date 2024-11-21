package com.zemoso.cashkick.cashkickApp.repository;

import com.zemoso.cashkick.cashkickApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String userName);
    Optional<User> findByEmail(String email);

    User findByUserid(Integer userId);
}
