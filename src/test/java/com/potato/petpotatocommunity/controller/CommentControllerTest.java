package com.potato.petpotatocommunity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.petpotatocommunity.dto.comment.CommentCreateRequest;
import com.potato.petpotatocommunity.dto.comment.CommentResultDto;
import com.potato.petpotatocommunity.dto.comment.CommentUpdateRequest;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    private UserDto mockUser() {
        UserDto user = new UserDto();
        user.setUserId(1L);
        user.setUsername("tester");
        return user;
    }

    @DisplayName("댓글 생성 성공")
    @Test
    void createComment_success() throws Exception {
        CommentCreateRequest request = CommentCreateRequest.builder()
                .postId(1L)
                .content("댓글 내용")
                .build();

        CommentResultDto resultDto = CommentResultDto.builder().result("success").build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", mockUser());

        when(commentService.createComment(any(), any())).thenReturn(resultDto);

        mockMvc.perform(post("/api/comments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));
    }

    @DisplayName("댓글 수정 성공")
    @Test
    void updateComment_success() throws Exception {
        Long commentId = 1L;
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("수정된 댓글")
                .build();

        CommentResultDto resultDto = CommentResultDto.builder().result("success").build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", mockUser());

        when(commentService.updateComment(any(), any(), any())).thenReturn(resultDto);

        mockMvc.perform(put("/api/comments/{commentId}", commentId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));
    }

    @DisplayName("댓글 삭제 성공")
    @Test
    void deleteComment_success() throws Exception {
        Long commentId = 1L;

        CommentResultDto resultDto = CommentResultDto.builder().result("success").build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", mockUser());

        when(commentService.deleteComment(any(), any())).thenReturn(resultDto);

        mockMvc.perform(delete("/api/comments/{commentId}", commentId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));
    }

    @DisplayName("댓글 목록 조회 성공")
    @Test
    void getComments_success() throws Exception {
        Long postId = 1L;

        CommentResultDto resultDto = CommentResultDto.builder()
                .result("success")
                .count(0)
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", mockUser());

        when(commentService.getCommentsByPostId(postId)).thenReturn(resultDto);

        mockMvc.perform(get("/api/comments/posts/{postId}", postId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));
    }

    @DisplayName("댓글 생성 실패 - 로그인하지 않은 사용자")
    @Test
    void createComment_unauthorized() throws Exception {
        CommentCreateRequest request = CommentCreateRequest.builder()
                .postId(1L)
                .content("비로그인 유저의 댓글")
                .build();

        mockMvc.perform(post("/api/comments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("댓글 수정 실패 - 로그인하지 않은 사용자")
    @Test
    void updateComment_unauthorized() throws Exception {
        Long commentId = 1L;
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("수정 시도")
                .build();

        mockMvc.perform(put("/api/comments/{commentId}", commentId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("댓글 삭제 실패 - 로그인하지 않은 사용자")
    @Test
    void deleteComment_unauthorized() throws Exception {
        Long commentId = 1L;

        mockMvc.perform(delete("/api/comments/{commentId}", commentId))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("댓글 수정 실패 - 존재하지 않는 댓글")
    @Test
    void updateComment_notFound() throws Exception {
        Long commentId = 999L;
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("없는 댓글 수정 시도")
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", mockUser());

        when(commentService.updateComment(any(), any(), any()))
                .thenThrow(new RuntimeException("댓글이 존재하지 않습니다."));

        mockMvc.perform(put("/api/comments/{commentId}", commentId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(session))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("서버 오류가 발생했습니다")));
    }

    @DisplayName("댓글 삭제 실패 - 권한 없음")
    @Test
    void deleteComment_forbidden() throws Exception {
        Long commentId = 1L;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", mockUser());

        when(commentService.deleteComment(any(), any()))
                .thenThrow(new RuntimeException("댓글 삭제 권한이 없습니다."));

        mockMvc.perform(delete("/api/comments/{commentId}", commentId)
                        .session(session))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("서버 오류가 발생했습니다")));
    }



}