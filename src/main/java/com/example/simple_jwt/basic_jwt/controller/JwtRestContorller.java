package com.example.simple_jwt.basic_jwt.controller;

import com.example.simple_jwt.basic_jwt.dto.UserDto;
import com.example.simple_jwt.basic_jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.*;

@RestController
@RequiredArgsConstructor
public class JwtRestContorller {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> save(@RequestBody @Valid UserDto userDto) {
        userService.save(userDto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @GetMapping("/user/join")
    public ResponseEntity<String> securityUserJoin() {
        return ResponseEntity.ok("USER 인증 완료");

    }

    @GetMapping("/admin/join")
    public ResponseEntity<String> securityAdminjoin() {
        return ResponseEntity.ok("ADMIN 인증 완료");
    }

}
