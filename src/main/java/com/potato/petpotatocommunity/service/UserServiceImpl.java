package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.exception.PasswordException;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.dto.user.UserProfileDto;
import com.potato.petpotatocommunity.entity.CommonCode;
import com.potato.petpotatocommunity.entity.User;
import com.potato.petpotatocommunity.repository.UserRepository;
import com.potato.petpotatocommunity.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void signUp(UserDto dto) {
        CommonCode role = userRoleRepository.findByCodeId(dto.getRoleId());

        User user = User.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .phone(dto.getPhone())
                .info(dto.getInfo())
                .profileImage(dto.getProfileImage())
                .build();

        userRepository.save(user);
    }

//    @Override
//    public UserDto findByEmail(String email) {
//        User user = userRepository.findByEmail(email).orElse(null);
//        if (user == null) return null;
//
//        UserDto dto = new UserDto();
//        dto.setUserId(user.getUserId());
//        dto.setEmail(user.getEmail());
//        dto.setUsername(user.getUsername());
//        dto.setNickname(user.getNickname());
//        dto.setPassword(user.getPassword());
//        dto.setRoleId(user.getRole().getCodeId());
//        dto.setPhone(user.getPhone());
//        dto.setInfo(user.getInfo());
//        dto.setProfileImage(user.getProfileImage());
//        return dto;
//    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return null;

        return mapUserToDto(user);
    }

    @Override
    public UserDto findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        return mapUserToDto(user);
    }

    @Override
    @Transactional
    public UserDto updateProfile(Long userId, UserProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // 프로필 정보 업데이트
        if (profileDto.getNickname() != null && !profileDto.getNickname().isEmpty()) {
            user.setNickname(profileDto.getNickname());
        }
        if (profileDto.getPhone() != null) {
            user.setPhone(profileDto.getPhone());
        }
        if (profileDto.getInfo() != null) {
            user.setInfo(profileDto.getInfo());
        }
        if (profileDto.getProfileImage() != null) {
            user.setProfileImage(profileDto.getProfileImage());
        }

        User updatedUser = userRepository.save(user);

        return mapUserToDto(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 검증
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new PasswordException("새 비밀번호를 입력해주세요.");
        }

        // 비밀번호 업데이트
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // 헬퍼 메소드: User 엔티티를 UserDto로 변환
    private UserDto mapUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setPassword(user.getPassword());
        dto.setRoleId(user.getRole().getCodeId());
        dto.setPhone(user.getPhone());
        dto.setInfo(user.getInfo());
        dto.setProfileImage(user.getProfileImage());
        return dto;
    }


}
