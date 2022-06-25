package com.example.jwt.controller;

import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.vo.User2;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @GetMapping("/home")
    public String home(){
        return "<h1>home<h1>";
    }

    @PostMapping("/token")
    public String token(){
        return "<h1>token<h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody User2 user2){
        user2.setPassword(bCryptPasswordEncoder.encode(user2.getPassword()));
        user2.setRoles("ROLE_USER");
        userRepository.save(user2);
        return "회원가입 완료 ! ";
    }

    @GetMapping("/api/v1/user")
    public String user(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println(principalDetails.getUsername());
        return "user";
    }
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }
}
