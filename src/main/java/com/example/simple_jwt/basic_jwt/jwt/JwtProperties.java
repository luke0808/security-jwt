package com.example.simple_jwt.basic_jwt.jwt;

public interface JwtProperties {
    String JWT_ACESS_HEADER = "accessToken";
    String JWT_REFRESH_HEADER = "refreshToken";
    String JWT_PREFIX = "Bearer ";
    String JWT_ACCESS_SECRET = "JwtTest";
    String JWT_REFRESH_SECRET = "JwtTest";
    int JWT_ACCESS_TOKEN_VALIDITY = 60000 * 1;//60000 = 1분
    int JWT_REFRESH_TOKEN_VALIDITY = 60000 * 2;//60000 = 1분
}
