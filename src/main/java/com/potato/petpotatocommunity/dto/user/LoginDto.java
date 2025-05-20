package com.potato.petpotatocommunity.dto.user;

import lombok.Data;

// login 요청 DTO ( 사용자 입력 이메일, 비밀번호 )

@Data
public class LoginDto {
    private String email;
    private String password;
}
