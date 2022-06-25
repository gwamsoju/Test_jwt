package com.example.jwt.config.auth;

import com.example.jwt.repository.UserRepository;
import com.example.jwt.vo.User2;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//http://localhost:8080/login -> 현재 formLogin.disable() 해놔서 동작을 안한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadUserByUsername()");
        User2 userEntity = userRepository.findByUsername(username);
        return new PrincipalDetails(userEntity);
    }
}
