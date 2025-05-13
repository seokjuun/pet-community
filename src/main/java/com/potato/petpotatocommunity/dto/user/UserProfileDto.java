package com.potato.petpotatocommunity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 마이페이지에서 수정 가능한 사용자 프로필 정보 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String nickname;     // 닉네임
    private String phone;        // 전화번호
    private String info;         // 자기소개
    private String profileImage; // 프로필 이미지 URL
}