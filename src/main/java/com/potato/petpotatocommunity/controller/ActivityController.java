package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.code.CodeDto;
import com.potato.petpotatocommunity.entity.Code;
import com.potato.petpotatocommunity.entity.CommonCode;
import com.potato.petpotatocommunity.exception.UnauthorizedException;
import com.potato.petpotatocommunity.dto.user.*;
import com.potato.petpotatocommunity.repository.CodeRepository;
import com.potato.petpotatocommunity.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Tag(name = "활동 내역 API", description = "사용자의 게시글, 댓글 등 활동 내역을 조회하는 API")
public class ActivityController {

    private final ActivityService activityService;
    private final CodeRepository codeRepository;

    @Operation(
            summary = "내 게시글 목록 조회",
            description = "이 API는 현재 로그인한 사용자의 게시글 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    @GetMapping("/posts")
    public ResponseDto<List<PostListDto>> getMyPosts(HttpSession session) {
        UserDto currentUser = (UserDto) session.getAttribute("user");

        if (currentUser == null) {
            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
        }

        List<PostListDto> posts = activityService.getPostsByUserId(currentUser.getUserId());
        return ResponseDto.success(posts);
    }

    @Operation(
            summary = "내 댓글 목록 조회",
            description = "이 API는 현재 로그인한 사용자의 댓글 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    @GetMapping("/comments")
    public ResponseDto<List<CommentListDto>> getMyComments(HttpSession session) {
        UserDto currentUser = (UserDto) session.getAttribute("user");

        if (currentUser == null) {
            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
        }

        List<CommentListDto> comments = activityService.getCommentsByUserId(currentUser.getUserId());
        return ResponseDto.success(comments);
    }

    @Operation(
            summary = "내 활동 통계 조회",
            description = "이 API는 현재 로그인한 사용자의 게시글 수, 댓글 수 등 활동 통계를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    @GetMapping("/stats")
    public ResponseDto<ActivityStatsDto> getMyActivityStats(HttpSession session) {
        UserDto currentUser = (UserDto) session.getAttribute("user");

        if (currentUser == null) {
            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
        }

        long postCount = activityService.countPostsByUserId(currentUser.getUserId());
        long commentCount = activityService.countCommentsByUserId(currentUser.getUserId());

        ActivityStatsDto stats = ActivityStatsDto.builder()
                .postCount(postCount)
                .commentCount(commentCount)
                .build();

        return ResponseDto.success(stats);
    }

    // 견종 드롭다운용
    @GetMapping("/breeds")
    public ResponseDto<List<CodeDto>> getBreedCodes() {
        List<Code> breedCodes = codeRepository.findByGroupCode("200");
        List<CodeDto> result = breedCodes.stream()
                .map(code -> new CodeDto(code.getGroupCode(), code.getCode(), code.getCodeName()))
                .toList();
        return ResponseDto.success(result);
    }

}
