package com.example.simple_jwt.basic_jwt.dto;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class UserDto {

    @NotBlank(message = "email을 입력하세요")
    private String email;
    private String password;

}
