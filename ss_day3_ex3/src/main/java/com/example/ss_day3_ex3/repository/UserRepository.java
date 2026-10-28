package com.example.ss_day3_ex3.repository;

import com.example.ss_day3_ex3.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u FROM User u WHERE u.username = :username
    """)
    Optional<User> findUserByUsername(String username);
}
