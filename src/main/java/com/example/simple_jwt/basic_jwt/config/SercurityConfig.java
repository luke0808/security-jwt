package com.example.simple_jwt.basic_jwt.config;

import com.example.simple_jwt.basic_jwt.jwt.JwtAuthenticationFilter;
import com.example.simple_jwt.basic_jwt.jwt.JwtAuthorizationFilter;
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

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
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
            httpSecurity.addFilter(new JwtAuthenticationFilter(authenticationManager));
            httpSecurity.addFilter(new JwtAuthorizationFilter(authenticationManager,userRepository));
        }
    }

}
