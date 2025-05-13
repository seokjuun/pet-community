package com.potato.petpotatocommunity.dto.user;

// 로그인 결과(성공 여부) DTO

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResultDto {
    private String result; // success / fail
    private UserDto userDto;
}
