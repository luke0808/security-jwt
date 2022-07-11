package com.example.simple_jwt.basic_jwt.jwt;

public interface JwtProperties {
    String JWT_HEADER = "Authorization";
    String JWT_PREFIX = "Bearer ";
    String JWT_SECRET = "JwtTest";
    int JWT_TOKEN_VALIDITY = 60000 * 1;//60000 = 1분
}
