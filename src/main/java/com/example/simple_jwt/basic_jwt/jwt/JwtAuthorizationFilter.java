package com.example.simple_jwt.basic_jwt.jwt;

import com.example.simple_jwt.basic_jwt.auth.CustomUserDetail;
import com.example.simple_jwt.basic_jwt.entity.RefreshToken;
import com.example.simple_jwt.basic_jwt.entity.Users;
import com.example.simple_jwt.basic_jwt.repository.RefreshTokenRepository;
import com.example.simple_jwt.basic_jwt.repository.UserRepository;
import io.jsonwebtoken.*;
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
    private RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,UserRepository userRepository,RefreshTokenRepository refreshTokenRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader("accessToken");

        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.JWT_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        if (isSecurityContextHolder(request,response)) chain.doFilter(request,response);
    }

    public boolean isSecurityContextHolder(HttpServletRequest request,HttpServletResponse response) {
        try {
            String accesstoken = request.getHeader(JwtProperties.JWT_ACESS_HEADER).replace(JwtProperties.JWT_PREFIX, "");
            String refreshtoken = "";

            //true : 만료
            //false : 유효
            boolean accesstokenExpriation = JwtTokenUtil.validateTokenExceptExpiration(JwtProperties.JWT_REFRESH_SECRET,accesstoken);
            System.out.println("accesstokenExpriation = " + accesstokenExpriation);

            if (accesstokenExpriation){

                refreshtoken = request.getHeader(JwtProperties.JWT_REFRESH_HEADER).replace(JwtProperties.JWT_PREFIX, "");

                if(!JwtTokenUtil.validateTokenExceptExpiration(JwtProperties.JWT_REFRESH_SECRET,refreshtoken)){
                    //accessToken다시 발행
                    //refreshToken으로 email추출!
                    Jws<Claims> refreshClaims = Jwts.parser().setSigningKey(JwtProperties.JWT_ACCESS_SECRET).parseClaimsJws(refreshtoken);

                    String refreshEmail = (String) refreshClaims.getBody().get("email");

                    JwtTokenUtil.createJwtToken(refreshEmail,JwtProperties.JWT_ACCESS_TOKEN_VALIDITY,JwtProperties.JWT_ACCESS_SECRET);

                    accesstoken = JwtTokenUtil.createJwtToken(refreshEmail,JwtProperties.JWT_ACCESS_TOKEN_VALIDITY,JwtProperties.JWT_ACCESS_SECRET);


                    //새로 accessToken을 front에 던져준다!!
                    response.addHeader("accessToken",accesstoken);

                    System.out.println("토큰 만료로 재발급");

                }else {
                    //refresh token도 만료 다시 로그인 시도하게끔
                    return false;
                }
            }
            
            Jws<Claims> claims = Jwts.parser().setSigningKey(JwtProperties.JWT_ACCESS_SECRET).parseClaimsJws(accesstoken);

            String email = (String) claims.getBody().get("email");

            if (email != null) {

                Users user = userRepository.findByEmail(email);

                CustomUserDetail customUserDetail = new CustomUserDetail(user);

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                customUserDetail,
                                customUserDetail.getPassword(),
                                customUserDetail.getAuthorities())
                );
            }
        } catch (ExpiredJwtException e){
            e.printStackTrace();
        }

        return true;
    }

}
