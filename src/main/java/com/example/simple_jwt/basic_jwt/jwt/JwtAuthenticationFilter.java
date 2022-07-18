package com.example.simple_jwt.basic_jwt.jwt;

import com.example.simple_jwt.basic_jwt.dto.UserDto;
import com.example.simple_jwt.basic_jwt.entity.RefreshToken;
import com.example.simple_jwt.basic_jwt.entity.Users;
import com.example.simple_jwt.basic_jwt.repository.RefreshTokenRepository;
import com.example.simple_jwt.basic_jwt.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserDto userDto = null;
        try {
            userDto = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword());
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        String accessToken = JwtTokenUtil.createJwtToken(authResult.getName(),JwtProperties.JWT_ACCESS_TOKEN_VALIDITY,JwtProperties.JWT_ACCESS_SECRET);
        String refreshToken = JwtTokenUtil.createJwtToken(authResult.getName(),JwtProperties.JWT_REFRESH_TOKEN_VALIDITY,JwtProperties.JWT_REFRESH_SECRET);

        Users users = userRepository.findByEmail(authResult.getName());

        RefreshToken refreshTokenEntity = refreshTokenRepository.findRefreshTokenById(users.getId());

        if (refreshTokenEntity == null){
            System.out.println("첫 생성!");
            saveRefreshToken(users, refreshToken);
        }else {
            System.out.println("여긴 널이 아니야!!");
            System.out.println("생성 후 체크!");
            boolean jwtValid = JwtTokenUtil.validateTokenExceptExpiration(JwtProperties.JWT_REFRESH_SECRET,refreshTokenEntity.getToken());
            if (jwtValid){
                System.out.println("만료");
                saveRefreshToken(users, refreshToken);
            }else {
                System.out.println("만료 아니면");
                refreshToken = refreshTokenEntity.getToken();
            }
        }

        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);

        response.addHeader("accessToken",JwtProperties.JWT_PREFIX+accessToken);
        response.addHeader("refreshToken",JwtProperties.JWT_PREFIX+refreshToken);
    }

    private void saveRefreshToken(Users users, String refreshToken) {
        refreshTokenRepository.save(RefreshToken.builder()
                .id(users.getId())
                .token(refreshToken)
                .build());
    }

}
