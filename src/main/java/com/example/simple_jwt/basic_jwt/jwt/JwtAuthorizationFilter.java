package com.example.simple_jwt.basic_jwt.jwt;

import com.example.simple_jwt.basic_jwt.auth.CustomUserDetail;
import com.example.simple_jwt.basic_jwt.entity.Users;
import com.example.simple_jwt.basic_jwt.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader("Authorization");

        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.JWT_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        if (isSecurityContextHolder(request)) chain.doFilter(request,response);
    }

    public boolean isSecurityContextHolder(HttpServletRequest request) {
        try {
            String token = request.getHeader(JwtProperties.JWT_HEADER).replace(JwtProperties.JWT_PREFIX, "");

            Claims playloads = Jwts.parser().setSigningKey(JwtProperties.JWT_SECRET).parseClaimsJws(token).getBody();

            String username = (String) playloads.get("username");

            if (username != null) {

                Users user = userRepository.findByUsername(username);

                CustomUserDetail customUserDetail = new CustomUserDetail(user);

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                customUserDetail,
                                customUserDetail.getPassword(),
                                customUserDetail.getAuthorities())
                );
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

}
