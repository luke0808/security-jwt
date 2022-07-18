package com.example.simple_jwt.basic_jwt.auth;

import com.example.simple_jwt.basic_jwt.entity.Users;
import com.example.simple_jwt.basic_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user =  userRepository.findByEmail(email);
        if (user.getEmail() == null && user.getPassword() == null) throw new UsernameNotFoundException(email);
        return new CustomUserDetail(user);
    }
}
