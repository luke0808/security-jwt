package com.example.simple_jwt.basic_jwt.service;

import com.example.simple_jwt.basic_jwt.dto.UserDto;
import com.example.simple_jwt.basic_jwt.entity.Users;
import com.example.simple_jwt.basic_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void save(UserDto userDto){
        userRepository.save(Users.createUsers(userDto));
    }
}
