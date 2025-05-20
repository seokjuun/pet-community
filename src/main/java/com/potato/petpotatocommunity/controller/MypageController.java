package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.user.ResponseDto;

import com.potato.petpotatocommunity.exception.UnauthorizedException;
import com.potato.petpotatocommunity.dto.user.PasswordChangeDto;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.dto.user.UserProfileDto;
import com.potato.petpotatocommunity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Tag(name = "마이페이지 API", description = "사용자 프로필 관리 및 비밀번호 변경 API")
public class MypageController {

    private final UserService userService;



    @Operation(
            summary = "프로필 정보 업데이트",
            description = "이 API는 사용자의 프로필 정보를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "업데이트 성공"),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    @PutMapping("/profile")
    public ResponseDto<UserDto> updateProfile(
            @RequestBody @Parameter(description = "업데이트할 프로필 정보") UserProfileDto profileDto,
            HttpSession session
    ) {
        UserDto currentUser = (UserDto) session.getAttribute("user");

        if (currentUser == null) {
            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
        }

        UserDto updatedUser = userService.updateProfile(currentUser.getUserId(), profileDto);

        // 세션 업데이트
        session.setAttribute("user", updatedUser);

        return ResponseDto.success(updatedUser, "프로필이 성공적으로 업데이트되었습니다.");
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "이 API는 사용자의 비밀번호를 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 비밀번호"),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    @PutMapping("/password")
    public ResponseDto<Void> changePassword(
            @RequestBody @Parameter(description = "비밀번호 변경 정보") PasswordChangeDto passwordChangeDto,
            HttpSession session
    ) {
        UserDto currentUser = (UserDto) session.getAttribute("user");

        if (currentUser == null) {
            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
        }

        userService.changePassword(
                currentUser.getUserId(),
                passwordChangeDto.getCurrentPassword(),
                passwordChangeDto.getNewPassword()
        );

        return ResponseDto.success(null, "비밀번호가 성공적으로 변경되었습니다.");
    }

//    @Operation(
//            summary = "사용자 활동 통계 조회",
//            description = "이 API는 사용자의 활동 통계(게시글, 댓글, 좋아요 수)를 조회합니다.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "통계 조회 성공"),
//                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
//            }
//    )
//    @GetMapping("/stats")
//    public ResponseDto<Object> getUserStats(HttpSession session) {
//        UserDto currentUser = (UserDto) session.getAttribute("user");
//
//        if (currentUser == null) {
//            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
//        }
//
//        // TODO: 실제 통계 서비스 구현 시 연결
//        // 임시 통계 데이터
//        var stats = new Object() {
//            public final int postCount = 0;
//            public final int commentCount = 0;
//            public final int likeCount = 0;
//        };
//
//        return ResponseDto.success(stats);
//    }
}