package com.example.simple_jwt.basic_jwt.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserDto {

    @NotBlank(message = "USERNAME을 입력하세요")
    private String username;
    private String password;

}
