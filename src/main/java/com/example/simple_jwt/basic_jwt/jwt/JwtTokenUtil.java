package com.example.simple_jwt.basic_jwt.jwt;

import com.example.simple_jwt.basic_jwt.auth.CustomUserDetail;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    public static String createJwtToken(String email, int expiration, String secret_key) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> playloads = new HashMap<>();
        playloads.put("email", email);

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(playloads)
                .setSubject("studyToken")
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret_key)
                .compact();
    }

    public static boolean validateTokenExceptExpiration(String tokenKey, String jwtToken) {
        try {

            Jws<Claims> claims = Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(jwtToken);

            return claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
