package com.example.simple_jwt.basic_jwt.repository;

import com.example.simple_jwt.basic_jwt.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

}
