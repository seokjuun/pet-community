package com.potato.petpotatocommunity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.petpotatocommunity.controller.MypageController;
import com.potato.petpotatocommunity.dto.user.PasswordChangeDto;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MypageController.class)
@Import(MypageControllerTest.TestConfig.class)
public class MypageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Configuration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    @Test
    void changePassword_success() throws Exception {
        // given
        PasswordChangeDto dto = new PasswordChangeDto();
        dto.setCurrentPassword("oldPass123");
        dto.setNewPassword("newPass456");

        UserDto mockUser = new UserDto();
        mockUser.setUserId(1L);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", mockUser);

        // when & then
        mockMvc.perform(put("/mypage/password")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"))
                .andExpect(jsonPath("$.message").value("비밀번호가 성공적으로 변경되었습니다."));

        // verify
        verify(userService).changePassword(1L, "oldPass123", "newPass456");
    }
}
