package com.example.simple_jwt.basic_jwt.jwt;

import com.example.simple_jwt.basic_jwt.auth.CustomUserDetail;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

        public static String createJwtToken(CustomUserDetail customUserDetail){
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> playloads = new HashMap<>();
        playloads.put("username",customUserDetail.getUsername());
        playloads.put("password",customUserDetail.getPassword());

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(playloads)
                .setSubject("studyToken")
                .setExpiration(new Date(System.currentTimeMillis()  +  JwtProperties.JWT_TOKEN_VALIDITY ))
                .signWith(SignatureAlgorithm.HS512, JwtProperties.JWT_SECRET)
                .compact();
        }
}
