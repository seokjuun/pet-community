package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.exception.LoginFailedException;
import com.potato.petpotatocommunity.dto.user.LoginDto;
import com.potato.petpotatocommunity.dto.user.LoginResultDto;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.dto.user.UserResultDto;
import com.potato.petpotatocommunity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpSession;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "인증 API", description = "사용자 로그인, 로그아웃 및 회원가입 관련 API")
public class LoginController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder; // 비밀번호 암호화

    @Operation(
            summary = "로그인",
            description = "이 API는 사용자가 이메일과 비밀번호로 로그인할 수 있게 해줍니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "401", description = "로그인 실패")
            }
    )

    @PostMapping("/login")
    public LoginResultDto login(
            @RequestBody @Parameter(description = "사용자 로그인 정보") LoginDto loginDto,
            HttpSession session
    ) {
        UserDto user = userService.findByEmail(loginDto.getEmail()); // 이메일로 사용자 정보 조회

//        if (user != null && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
//            session.setAttribute("user", user); // 세션에 사용자 저장
//            return new LoginResultDto("success", user); // 로그인 성공
//        } else {
//            return new LoginResultDto("fail", null); // 로그인 실패
//        }

        if (user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new LoginFailedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        session.setAttribute("user", user); // 세션에 사용자 저장
        return new LoginResultDto("success", user); // 로그인 성공
    }


    @Operation(
            summary = "로그아웃",
            description = "이 API는 사용자가 로그아웃할 수 있게 해줍니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
            }
    )

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화(로그아웃)

        return "logout success"; // 로그아웃 응답
    }

    @Operation(
            summary = "현재 로그인된 사용자 조회",
            description = "이 API는 클라이언트가 현재 로그인된 사용자의 정보를 조회할 수 있게 해줍니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            }
    )

    @GetMapping("/me") // 로그아웃 후 확인 시 세션 무효화로 정보 없음, 로그인 시 조회시 사용자 정보 존재
    public UserDto currentUser(HttpSession session) {
        return (UserDto) session.getAttribute("user");
    }

    @Operation(
            summary = "회원가입",
            description = "이 API는 사용자가 새로운 계정을 생성할 수 있게 해줍니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공"),
                    @ApiResponse(responseCode = "400", description = "회원가입 실패")
            }
    )

    @PostMapping("/register")
    public UserResultDto register(@RequestBody UserDto userDto) {
        try {
            userService.signUp(userDto); // 사용자 회원가입 처리
            return new UserResultDto("success", userDto); // 회원가입 성공 응답
        } catch (Exception e) {
            return new UserResultDto("fail", null); // 회원가입 실패 시 실패 응답
        }
    }
}
