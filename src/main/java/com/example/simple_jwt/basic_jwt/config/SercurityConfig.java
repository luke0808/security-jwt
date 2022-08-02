package com.example.simple_jwt.basic_jwt.config;

import com.example.simple_jwt.basic_jwt.jwt.JwtAuthenticationFilter;
import com.example.simple_jwt.basic_jwt.jwt.JwtAuthorizationFilter;
import com.example.simple_jwt.basic_jwt.repository.RefreshTokenRepository;
import com.example.simple_jwt.basic_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SercurityConfig {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()  //기본 인증 로그인 방식 해제
                .formLogin().disable()  //form login 로그인 설정 해제
                .csrf().disable()   //csrf는 api서버를 사용하기 때문에 미사용
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/user/join")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/admin/join")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .apply(new MyCustomDsl())
                .and()
                .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity httpSecurity) throws Exception {
            AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
            httpSecurity.addFilter(new JwtAuthenticationFilter(authenticationManager,userRepository,refreshTokenRepository));
            httpSecurity.addFilter(new JwtAuthorizationFilter(authenticationManager,userRepository,refreshTokenRepository));
        }
    }

}
