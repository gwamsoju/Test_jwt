package com.example.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.vo.User2;
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
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// /login 요청해서 Username, password 전송하면
// UsernamePasswordAuthenticationFilter가 동작한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;


    // /login 요청을 하면 로그인 시도를 위해 실행되는 함수.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중");

        // 1. username, password 받아서
        try {

//            BufferedReader br =   request.getReader();
//            String input = null;
//            while((input = br.readLine()) != null){
//                System.out.println(input);
//            }
            ObjectMapper om = new ObjectMapper();
            User2 user2 = om.readValue(request.getInputStream(), User2.class);
            System.out.println(user2);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user2.getUsername(),user2.getPassword());
            
            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            // authentication 객체가 session 영역에 저장됨. -> 로그인이 되었다는 뜻.
            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principal.getUser2().getUsername());
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("====================");
        // 2. 정상인지 로그인 시도를 한다. authenticationManager로 로그인 시도를 하면
        // PrincipalDetailsService가 호출 loadUserByUsername() 함수 실행됨.

        // 3. PrincipalDetails 세션에 담고 ( 권한 관리를 위해서 )

        // 4. JWT토큰을 만들어서 응답해주면 됨.
        return null;
    }

    // attemptAuthentication 인증이 정상적으로 됐으면 successfulAuthentication()가 실행된다.
    // jwt 토큰을 만들어서 request 요청한 사용자에게 토큰을 응답해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        System.out.println("인증 완료됨.");
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();

        // 라이브러리 사용, RSA 방식은 아니고 Hash암호방식
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis()+ (60000 * 10))) // 토큰 만료 시간을 10분으로 설정함.
                .withClaim("id", principal.getUser2().getId())
                .withClaim("username", principal.getUser2().getUsername())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization","Bearer " + jwtToken);
    }
}
