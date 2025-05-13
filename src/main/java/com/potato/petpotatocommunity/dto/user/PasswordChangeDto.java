package com.potato.petpotatocommunity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 비밀번호 변경 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDto {
    private String currentPassword; // 현재 비밀번호
    private String newPassword;     // 새 비밀번호
}