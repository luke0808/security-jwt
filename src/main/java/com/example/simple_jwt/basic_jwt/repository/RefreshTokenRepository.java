package com.example.simple_jwt.basic_jwt.repository;

import com.example.simple_jwt.basic_jwt.entity.RefreshToken;
import com.example.simple_jwt.basic_jwt.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findRefreshTokenById(Long id);

}
