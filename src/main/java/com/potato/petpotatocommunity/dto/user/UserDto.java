package com.potato.petpotatocommunity.dto.user;

import lombok.Builder;
import lombok.Data;

// 회원가입, 사용자 정보 전송 DTO

@Data
public class UserDto {
    private Long userId;         // 사용자 ID
    private String email;       // 이메일
    private String username;    // 사용자명
    private String password;    // 비밀번호
    private String nickname;    // 닉네임
    private String roleId;      // 역할 ID (CommonCode의 codeId)
    private String phone;       // 전화번호
    private String info;        // 간단한 소개
    private String profileImage; // 프로필 이미지 URL
}

