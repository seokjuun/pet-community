//package com.potato.petpotatocommunity.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.potato.petpotatocommunity.dto.post.*;
//import com.potato.petpotatocommunity.dto.user.UserDto;
//import com.potato.petpotatocommunity.service.PostLikeService;
//import com.potato.petpotatocommunity.service.PostService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import(PostControllerTest.MockServiceConfig.class)
//class PostControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private PostLikeService postLikeService;
//
//    @TestConfiguration
//    static class MockServiceConfig {
//        @Bean
//        public PostService postService() {
//            return Mockito.mock(PostService.class);
//        }
//
//        @Bean
//        public PostLikeService postLikeService() {
//            return Mockito.mock(PostLikeService.class);
//        }
//    }
//
//
//
//    private UserDto createMockUser() {
//        UserDto user = new UserDto();
//        user.setUserId(1L);
//        user.setUsername("testuser");
//        user.setEmail("test@example.com");
//        user.setNickname("테스터");
//        user.setPassword("encoded-password");
//        user.setRoleId("ROLE_USER");
//        user.setPhone("010-0000-0000");
//        user.setInfo("테스트 사용자");
//        user.setProfileImage("/images/profile1.png");
//        return user;
//    }
//
//    @DisplayName("게시글 생성 성공")
//    @Test
//    void createPost_success() throws Exception {
//        // given
//        PostCreateRequest request = PostCreateRequest.builder()
//                .hashtagId("CATEGORY_GENERAL")
//                .title("테스트 제목")
//                .content("테스트 내용")
//                .build();
//
//        UserDto userDto = createMockUser();
//
//        PostResultDto resultDto = PostResultDto.builder()
//                .result("success")
//                .build();
//
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("user", userDto);
//
//        when(postService.createPost(any(), any(), any()))
//                .thenReturn(resultDto);
//
//        MockMultipartFile imageFile = new MockMultipartFile("images", "success_image.jpg", "image/jpeg", new byte[0]);
//        MockMultipartFile postPart = new MockMultipartFile("post", "", "application/json", objectMapper.writeValueAsBytes(request));
//
//        // when
//        ResultActions result = mockMvc.perform(multipart("/api/posts")
//                .file(imageFile)
//                .file(postPart)
//                .session(session)
//                .contentType(MediaType.MULTIPART_FORM_DATA));
//
//        // then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("success"));
//    }
//
//    @DisplayName("게시글 단건 조회 성공")
//    @Test
//    void getPost_success() throws Exception {
//        Long postId = 1L;
//        UserDto userDto = createMockUser();
//
//        PostDetailResponse detail = PostDetailResponse.builder()
//                .postId(postId)
//                .title("제목")
//                .content("내용")
//                .username(userDto.getUsername())
//                .userId(userDto.getUserId())
//                .hashtagId("CATEGORY_GENERAL")
//                .hashtagName("일반")
//                .viewCount(0)
//                .imageUrls(Collections.emptyList())
//                .build();
//
//        PostResultDto resultDto = PostResultDto.builder()
//                .result("success")
//                .postDetailResponse(detail)
//                .build();
//
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("user", userDto);
//
//        when(postService.getPost(eq(postId), eq(userDto))).thenReturn(resultDto);
//        when(postLikeService.getLikeCount(postId)).thenReturn(5L);
//        when(postLikeService.isLiked(postId, userDto.getUserId())).thenReturn(true);
//
//        // when
//        ResultActions result = mockMvc.perform(get("/api/posts/{postId}", postId)
//                .session(session));
//
//        // then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.postDetailResponse.postId").value(postId))
//                .andExpect(jsonPath("$.postDetailResponse.likeCount").value(5))
//                .andExpect(jsonPath("$.postDetailResponse.liked").value(true));
//    }
//
//    @DisplayName("게시글 좋아요 토글 성공")
//    @Test
//    void toggleLike_success() throws Exception {
//        Long postId = 1L;
//        UserDto userDto = createMockUser();
//
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("user", userDto);
//
//        when(postLikeService.toggleLike(postId, userDto.getUserId())).thenReturn(true);
//
//        ResultActions result = mockMvc.perform(post("/api/posts/{postId}/like", postId)
//                .session(session));
//
//        result.andExpect(status().isOk())
//                .andExpect(content().string("좋아요 추가"));
//    }
//
//    @DisplayName("게시글 단건 조회 실패 - 존재하지 않는 ID")
//    @Test
//    void getPost_fail_notFound() throws Exception {
//        Long postId = 999L;
//        UserDto userDto = createMockUser();
//
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("user", userDto);
//
//        when(postService.getPost(postId, userDto))
//                .thenThrow(new RuntimeException("존재하지 않는 게시글입니다."));
//
//        ResultActions result = mockMvc.perform(get("/api/posts/{postId}", postId)
//                .session(session));
//
//        result.andExpect(status().isInternalServerError())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("서버 오류가 발생했습니다")));
//    }
//
//    @DisplayName("게시글 수정 성공")
//    @Test
//    void updatePost_success() throws Exception {
//        Long postId = 1L;
//        UserDto userDto = createMockUser();
//
//        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
//                .title("수정된 제목")
//                .content("수정된 내용")
//                .hashtagId("CATEGORY_GENERAL")
//                .build();
//
//        PostResultDto resultDto = PostResultDto.builder()
//                .result("success")
//                .build();
//
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("user", userDto);
//
//        when(postService.updatePost(anyLong(), any(PostUpdateRequest.class), any(), eq(userDto))).thenReturn(resultDto);
//
//        MockMultipartFile postPart = new MockMultipartFile("post", "", "application/json", objectMapper.writeValueAsBytes(updateRequest));
//
//        ResultActions result = mockMvc.perform(multipart("/api/posts/{postId}", postId)
//                .file(postPart)
//                .with(request -> { request.setMethod("PUT"); return request; }) // PUT으로 변형
//                .session(session)
//                .contentType(MediaType.MULTIPART_FORM_DATA));
//
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("success"));
//    }
//
//    @DisplayName("게시글 삭제 성공")
//    @Test
//    void deletePost_success() throws Exception {
//        Long postId = 1L;
//        UserDto userDto = createMockUser();
//
//        PostResultDto resultDto = PostResultDto.builder()
//                .result("success")
//                .build();
//
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("user", userDto);
//
//        when(postService.deletePost(eq(postId), eq(userDto))).thenReturn(resultDto);
//
//        ResultActions result = mockMvc.perform(delete("/api/posts/{postId}", postId)
//                .session(session));
//
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("success"));
//    }
//
//    @DisplayName("게시글 목록 조회 성공")
//    @Test
//    void getPosts_success() throws Exception {
//        PostDetailResponse post = PostDetailResponse.builder()
//                .postId(1L)
//                .title("제목")
//                .username("작성자")
//                .userId(1L)
//                .hashtagName("일반")
//                .viewCount(10)
//                .build();
//
//        PostResultDto resultDto = PostResultDto.builder()
//                .result("success")
//                .postList(Collections.singletonList(post))
//                .count(1L)
//                .build();
//
//        when(postService.getPosts(anyInt(), anyInt(), any(), any())).thenReturn(resultDto);
//
//        ResultActions result = mockMvc.perform(get("/api/posts")
//                .param("page", "0")
//                .param("size", "10"));
//
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("success"))
//                .andExpect(jsonPath("$.postList[0].postId").value(1L))
//                .andExpect(jsonPath("$.count").value(1));
//    }
//
//    @DisplayName("인기 게시글 조회 성공")
//    @Test
//    void getPopularPosts_success() throws Exception {
//        PostDetailResponse post = PostDetailResponse.builder()
//                .postId(1L)
//                .title("인기글")
//                .username("인기작성자")
//                .userId(1L)
//                .hashtagName("인기")
//                .viewCount(100)
//                .likeCount(10L)
//                .build();
//
//        PostResultDto resultDto = PostResultDto.builder()
//                .result("success")
//                .postList(Collections.singletonList(post))
//                .count(1L)
//                .build();
//
//        when(postService.getPopularPosts(anyInt(), anyInt())).thenReturn(resultDto);
//
//        ResultActions result = mockMvc.perform(get("/api/posts/popular")
//                .param("page", "0")
//                .param("size", "4"));
//
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("success"))
//                .andExpect(jsonPath("$.postList[0].title").value("인기글"));
//    }
//
//    @DisplayName("해시태그별 인기 게시글 조회 성공")
//    @Test
//    void getPopularPostsByHashtag_success() throws Exception {
//        PostDetailResponse post = PostDetailResponse.builder()
//                .postId(1L)
//                .title("해시태그 인기글")
//                .username("작성자")
//                .userId(1L)
//                .hashtagName("산책")
//                .viewCount(50)
//                .likeCount(8L)
//                .build();
//
//        PostResultDto resultDto = PostResultDto.builder()
//                .result("success")
//                .postList(Collections.singletonList(post))
//                .count(1L)
//                .build();
//
//        when(postService.getPopularPostsByHashtag(anyString(), anyInt(), anyInt())).thenReturn(resultDto);
//
//        ResultActions result = mockMvc.perform(get("/api/posts/popular/hashtag/{hashtagId}", "CATEGORY_WALK")
//                .param("page", "0")
//                .param("size", "4"));
//
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("success"))
//                .andExpect(jsonPath("$.postList[0].hashtagName").value("산책"));
//    }
//
//    @DisplayName("게시글 생성 실패 - 최대 이미지 개수 초과")
//    @Test
//    void createPost_fail_tooManyImages() throws Exception {
//        PostCreateRequest request = PostCreateRequest.builder()
//                .hashtagId("CATEGORY_GENERAL")
//                .title("테스트 제목")
//                .content("테스트 내용")
//                .build();
//
//        UserDto userDto = createMockUser();
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("user", userDto);
//
//        when(postService.createPost(any(), any(), any()))
//                .thenThrow(new RuntimeException("이미지는 최대 5장까지 업로드 가능합니다."));
//
//
//        // Use 6 unique image files for test isolation
//        MockMultipartFile image1 = new MockMultipartFile("images", "fail_image1.jpg", "image/jpeg", new byte[0]);
//        MockMultipartFile image2 = new MockMultipartFile("images", "fail_image2.jpg", "image/jpeg", new byte[0]);
//        MockMultipartFile image3 = new MockMultipartFile("images", "fail_image3.jpg", "image/jpeg", new byte[0]);
//        MockMultipartFile image4 = new MockMultipartFile("images", "fail_image4.jpg", "image/jpeg", new byte[0]);
//        MockMultipartFile image5 = new MockMultipartFile("images", "fail_image5.jpg", "image/jpeg", new byte[0]);
//        MockMultipartFile image6 = new MockMultipartFile("images", "fail_image6.jpg", "image/jpeg", new byte[0]);
//        MockMultipartFile postPart = new MockMultipartFile("post", "", "application/json", objectMapper.writeValueAsBytes(request));
//
//        ResultActions result = mockMvc.perform(multipart("/api/posts")
//                .file(image1)
//                .file(image2)
//                .file(image3)
//                .file(image4)
//                .file(image5)
//                .file(image6)
//                .file(postPart)
//                .session(session)
//                .contentType(MediaType.MULTIPART_FORM_DATA));
//
//        result.andExpect(status().isInternalServerError())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("서버 오류가 발생했습니다")));
//
//        // 테스트 종료 후 mock 상태 초기화
//        Mockito.reset(postService);
//    }
//
//    @DisplayName("게시글 수정 실패 - 권한 없음")
//    @Test
//    void updatePost_fail_noPermission() throws Exception {
//        Long postId = 2L;
//        UserDto userDto = createMockUser();
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("user", userDto);
//
//        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
//                .title("수정제목")
//                .content("수정내용")
//                .hashtagId("CATEGORY_GENERAL")
//                .build();
//
//        when(postService.updatePost(anyLong(), any(PostUpdateRequest.class), any(), eq(userDto)))
//                .thenThrow(new RuntimeException("게시글을 수정할 권한이 없습니다."));
//
//        MockMultipartFile postPart = new MockMultipartFile("post", "", "application/json", objectMapper.writeValueAsBytes(updateRequest));
//
//        ResultActions result = mockMvc.perform(multipart("/api/posts/{postId}", postId)
//                .file(postPart)
//                .with(request -> { request.setMethod("PUT"); return request; })
//                .session(session)
//                .contentType(MediaType.MULTIPART_FORM_DATA));
//
//        result.andExpect(status().isInternalServerError())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("서버 오류가 발생했습니다")));
//    }
//
//    @DisplayName("게시글 삭제 실패 - 게시글 없음")
//    @Test
//    void deletePost_fail_notFound() throws Exception {
//        Long postId = 999L;
//        UserDto userDto = createMockUser();
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("user", userDto);
//
//        when(postService.deletePost(eq(postId), eq(userDto)))
//                .thenThrow(new RuntimeException("존재하지 않는 게시글입니다."));
//
//        ResultActions result = mockMvc.perform(delete("/api/posts/{postId}", postId)
//                .session(session));
//
//        result.andExpect(status().isInternalServerError())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("서버 오류가 발생했습니다")));
//    }
//}