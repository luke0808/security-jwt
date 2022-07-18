package com.example.simple_jwt.basic_jwt.entity;

import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private long id;

    private String token;

    @Builder
    public RefreshToken(long id, String token) {
        Assert.notNull(id,"Id must not be empty");
        Assert.hasText(token,"Id must not be empty");
        this.id = id;
        this.token = token;
    }

}
