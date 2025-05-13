package com.potato.petpotatocommunity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원가입 결과 DTO

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserResultDto {
    private String result;     // 결과 (성공/실패)
    private UserDto userDto;   // 사용자 정보 (회원가입 시 사용)
}
