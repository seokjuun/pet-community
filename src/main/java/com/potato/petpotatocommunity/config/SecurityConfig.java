package com.potato.petpotatocommunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf 비활성화
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청 허용
                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll() // /auth/ 로 시작하는 요청 인증 없이 접근 허용
//                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // swagger 허용
//                        .requestMatchers("/manager/**").hasRole("MANAGER") // 관리자만 접근 가능
//                        .requestMatchers("/user/**").hasRole("USER") // 사용자만 접근 가능
//                        .requestMatchers("/",
//                                         "/index.html",
//                                         "/login.html",
//                                         "/register.html",
//                                         "/mypage.html",
//                                         "/assets/**",
//                                         "/mypage/**").permitAll()
//                        .anyRequest().authenticated() // 나머지 요청은 로그인 필요
//
//
//                )
                .formLogin(Customizer.withDefaults()) // 기본 제공 Spring Security 로그인 폼 활성화
                .httpBasic(Customizer.withDefaults()) // Http Basic 인증 활성화
                .logout(logout -> logout.disable()); // 필요 없다면 disable 가능

        return http.build();
    }

    @Bean
    // 비밀번호 암호화
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll() // 모든 요청 허용
//                );
//        return http.build();
//    }
//}