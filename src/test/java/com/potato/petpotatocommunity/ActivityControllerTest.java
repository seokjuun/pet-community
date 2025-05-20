package com.potato.petpotatocommunity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.petpotatocommunity.controller.ActivityController;
import com.potato.petpotatocommunity.controller.LoginController;
import com.potato.petpotatocommunity.dto.user.*;
import com.potato.petpotatocommunity.exception.LoginFailedException;
import com.potato.petpotatocommunity.exception.UnauthorizedException;
import com.potato.petpotatocommunity.service.ActivityService;
import com.potato.petpotatocommunity.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ActivityControllerTest {

    // private MockMvc mockMvc;

    @InjectMocks
    private ActivityController activityController;

    @Mock
    private ActivityService activityService;

    @Mock
    private HttpSession session;

    private UserDto mockUser;

    @BeforeEach
    void setup() {
        mockUser = new UserDto();
        mockUser.setUserId(1L);
    }

    // 마이페이지 본인 작성 게시글 성공적으로 불러오는 경우
    @Test
    public void testGetMyPosts_Success() {
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(activityService.getPostsByUserId(1L)).thenReturn(List.of(new PostListDto()));

        ResponseDto<List<PostListDto>> response = activityController.getMyPosts(session);

        assertNotNull(response);
        assertEquals("success", response.getResult());
        assertFalse(response.getData().isEmpty());
    }

    // 마이페이지 본인 작성 게시글 불러올 때 로그인 하지 않은 경우
    @Test
    public void testGetMyPosts_Unauthorized() {
        when(session.getAttribute("user")).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> {
            activityController.getMyPosts(session);
        });
    }

    // 마이페이지 본인 작성 댓글 정상적으로 불러오는 경우
    @Test
    public void testGetMyComments_Success() {
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(activityService.getCommentsByUserId(1L)).thenReturn(List.of(new CommentListDto()));

        ResponseDto<List<CommentListDto>> response = activityController.getMyComments(session);

        assertNotNull(response);
        assertEquals("success", response.getResult());
        assertFalse(response.getData().isEmpty());
    }

    // 마이페이지 본인 작성 댓글 불러올 때 로그인 하지 않은 경우
    @Test
    public void testGetMyComments_Unauthorized() {
        when(session.getAttribute("user")).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> {
            activityController.getMyComments(session);
        });
    }

    // 마이페이지 본인 작성 글 / 댓글 수 불러오는 경우
    @Test
    public void testGetMyStats_Success() {
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(activityService.countPostsByUserId(1L)).thenReturn(5L);
        when(activityService.countCommentsByUserId(1L)).thenReturn(10L);

        ResponseDto<ActivityStatsDto> response = activityController.getMyActivityStats(session);

        assertNotNull(response);
        assertEquals("success", response.getResult());
        assertEquals(5L, response.getData().getPostCount());
        assertEquals(10L, response.getData().getCommentCount());
    }

    // 마이페이지 본인 작성 글 / 댓글 수 불러올 때 로그인 되어 있지 않은 경우
    @Test
    public void testGetMyStats_Unauthorized() {
        when(session.getAttribute("user")).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> {
            activityController.getMyActivityStats(session);
        });
    }



}