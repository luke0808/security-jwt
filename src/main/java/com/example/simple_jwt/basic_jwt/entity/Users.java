package com.example.simple_jwt.basic_jwt.entity;

import com.example.simple_jwt.basic_jwt.authenum.RoleEnum;
import com.example.simple_jwt.basic_jwt.dto.UserDto;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum roles;

    public static Users createUsers(UserDto userDto) {
        Users users = new Users();
        users.email = userDto.getEmail();
        users.password = new BCryptPasswordEncoder().encode(userDto.getPassword());
        users.roles = RoleEnum.USER;
        return users;
    }
}
