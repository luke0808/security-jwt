package com.example.simple_jwt.basic_jwt.jwt;

import com.auth0.jwt.JWT;
import com.example.simple_jwt.basic_jwt.auth.CustomUserDetail;
import com.example.simple_jwt.basic_jwt.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserDto userDto = null;
        try {
            userDto = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword());
        return authenticationManager.authenticate(token); //세션 영역에 저장됨.
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        System.out.println("인증 성공");
        String jwtToken = JwtTokenUtil.createJwtToken((CustomUserDetail) authResult.getPrincipal());
        response.addHeader(JwtProperties.JWT_HEADER,JwtProperties.JWT_PREFIX+jwtToken);
    }
}
