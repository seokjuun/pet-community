package com.potato.petpotatocommunity.service;

// 회원가입 및 사용자 조회

import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.dto.user.UserProfileDto;
import com.potato.petpotatocommunity.exception.PasswordException;

public interface UserService {
    void signUp(UserDto userDto);
    UserDto findByEmail(String email); // 로그인 시 사용

    /**
     * 사용자 ID로 사용자를 찾습니다.
     *
     * @param userId 사용자 ID
     * @return 찾은 사용자 정보
     */
    UserDto findById(Long userId);

    /**
     * 사용자 프로필을 업데이트합니다.
     *
     * @param userId 사용자 ID
     * @param profileDto 업데이트할 프로필 정보
     * @return 업데이트된 사용자 정보
     */
    UserDto updateProfile(Long userId, UserProfileDto profileDto);

    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param userId 사용자 ID
     * @param currentPassword 현재 비밀번호
     * @param newPassword 새 비밀번호
     * @throws PasswordException 현재 비밀번호가 일치하지 않을 경우
     */
    void changePassword(Long userId, String currentPassword, String newPassword);

//    /**
//     * 사용자 계정을 삭제합니다.
//     *
//     * @param userId 삭제할 사용자 ID
//     */
//    void deleteAccount(Long userId);

}
