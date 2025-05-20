package com.potato.petpotatocommunity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.petpotatocommunity.controller.LoginController;
import com.potato.petpotatocommunity.dto.user.LoginDto;
import com.potato.petpotatocommunity.dto.user.LoginResultDto;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.dto.user.UserResultDto;
import com.potato.petpotatocommunity.exception.LoginFailedException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoginController loginController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSuccessfulLogin() {
        // Arrange : 테스트용 로그인 DTO 객체 생성
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com"); // 테스트용 이메일 설정
        loginDto.setPassword("password123"); // 테스트용 비밀번호 설정

        // Arrange : 서비스가 반환할 사용자의 정보를 담은 DTO 객체 생성
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com"); // 동일 이메일 설정
        userDto.setPassword("encodedPassword"); // 암호화된 비밀번호 ( 실제 비밀번호 X )
        userDto.setUserId(1L); // 사용자 ID 설정

        // Mock the behavior : userService.findByEmail 호출 시 userDto 반환하도록 설정
        when(userService.findByEmail("test@example.com")).thenReturn(userDto);
        // Mock the behavior : passwordEncoder.matches 호출 시 true 반환 ( 비밀번호 일치한다고 가정)
        when(passwordEncoder.matches("password123", userDto.getPassword())).thenReturn(true);

        // Act : LoginController 의 login 메서드 실행 ( 로그인 시도 )
        LoginResultDto result = loginController.login(loginDto, session);

        // Assert : 반환된 결과가 null 이 아닌지 확인 ( 로그인 성공 여부)
        assertNotNull(result);

        // Assert : 로그인 결과(result) 에 "success" 인지 확인
        assertEquals("success", result.getResult());

        // Assert : 로그인 결과에 포함된 유저 정보가 예상한 userDto 와 일치하는지 확인
        assertEquals(userDto, result.getUserDto());

        // Verify : 세션에 "user" 라는 이름으로 userDto 가 저장되어있는 지 확인
        verify(session).setAttribute("user", userDto);
    }

    @Test
    public void testLoginFailedInvalidEmail() {
        // Arrange
        LoginDto loginDto = new LoginDto(); // 로그인 정보 객체 생성
        loginDto.setEmail("nonexistent@example.com"); // 존재하지 않는 이메일로 설정
        loginDto.setPassword("password123"); // 임의의 비밀번호 설정

        // Mock the behavior : userService 가 입력한 이메일을 찾지 못하는 경우 null 반환
        when(userService.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert : login 메서드 실해 시 LoginFailedException 이 발생하는 확인
        assertThrows(LoginFailedException.class, () -> {
            loginController.login(loginDto, session); // 로그인 시도 -> 실패 -> 예외 발생해야함
        });
    }

    @Test
    public void testLoginFailedInvalidPassword() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com"); // 이메일 설정 ( 존재하는 사용자의 이메일 )
        loginDto.setPassword("wrongpassword"); // 잘못된 비밀번호 설정

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com"); // 입력한 이메일
        userDto.setPassword("encodedPassword"); // 잘못된 비밀번호가 저장됨

        // Mock the behavior
        // 이메일로 사용자 조회 시 userDto 반환되도록 설정 ( 이메일 정보는 일치함 )
        when(userService.findByEmail("test@example.com")).thenReturn(userDto);

        // 비밀번호 비교 시 실패하도록 설정 ( 입력한 비밀번호와 저장된 비밀번호가 일치하지 않음 )
        when(passwordEncoder.matches("wrongpassword", userDto.getPassword())).thenReturn(false);

        // Act & Assert
        // login 메소드 호출 시 LoginFailException 이 발생하는지 확인
        assertThrows(LoginFailedException.class, () -> {
            loginController.login(loginDto, session);
        });
    }

    @Test
    public void testLogout() {
        // Act : 컨트롤러의 logout 메소드 호출하여 로그아웃 처리
        String result = loginController.logout(session);

        // Assert : 로그아웃 결과 문자열이 "logout success" 인지 확인
        assertEquals("logout success", result);

        // session 의 invalidate() 메서드가 호출되었는지 확인
        // 세션이 무효화 되었는지 검증
        verify(session).invalidate();
    }

    @Test
    public void testCurrentUserWhenLoggedIn() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com"); // 이메일 설정

        // Mock the behavior : session 에서 "user" 라는 key 로 userDto 반환하도록 설정 ( 로그인 상태 가정 )
        when(session.getAttribute("user")).thenReturn(userDto);

        // Act : 로그인 된 유저 정보를 가져오는 메서드 호출
        UserDto result = loginController.currentUser(session);

        // Assert : 결과로 반환된 suerDto 가 위의 suerDto 와 같은지 확인
        assertEquals(userDto, result);
    }

    @Test
    public void testCurrentUserWhenNotLoggedIn() {
        // Arrange : session 에 "user" 라는 key 에 속성 없도록 설정 ( 로그인 하지 않은 상태)
        when(session.getAttribute("user")).thenReturn(null);

        // Act : 로그인한 사용자 정보를 가져오는 메서드 호출
        // 로그인 되어 있지 않으므로 null 발생 예상
        UserDto result = loginController.currentUser(session);

        // Assert
        // 반환값이 null 인지 확인 ( 로그인 안 했을 때의 예상 결과 )
        assertNull(result);
    }

    @Test
    public void testRegisterSuccess() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("newuser@example.com"); // 이메일 설정
        userDto.setPassword("password123"); // 비밀번호 설정

        // Mock the behavior
        // signup 메서드 실행 시  아무런 동작도 하지 않도록 설정 ( doNoting().when )
        doNothing().when(userService).signUp(userDto);

        // Act : LoginController 의 register 메서드 호출하여 회원 가입 시도
        UserResultDto result = loginController.register(userDto);

        // Assert
        // 결과가 null 이 아니어야 함.
        assertNotNull(result);
        // result 값이 "success" 와 동일한지 확인 ( 회원 가입 성공 확인 )
        assertEquals("success", result.getResult());
        // result 에서 반환된 userDto 가 입력된 userDto 와 동일한지 확인
        assertEquals(userDto, result.getUserDto());
        // userService 의 signup 메서드 호출됐는지 확인
        verify(userService).signUp(userDto);
    }

    @Test
    public void testRegisterFailure() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("newuser@example.com"); // 이메일 설정
        userDto.setPassword("password123"); // 비밀번호 설정

        // Mock the behavior
        //userService.signup(userDto) 가 호출되면 RuntimeExcpetion 을 던지도록 설정 ( 회원가입 실패 상황 )
        doThrow(new RuntimeException("Registration failed")).when(userService).signUp(userDto);

        // Act
        // 회원가입 메서드 호출
        UserResultDto result = loginController.register(userDto);

        // Assert
        assertNotNull(result); // 결과가 null 이 아닌지 확인
        assertEquals("fail", result.getResult()); // 결과 메시지가 "fail" 인지 확인
        assertNull(result.getUserDto()); // 회원가입 실패 시 userDto 는 null 이어야 한다.
    }

    // MockMvc integration tests
    @Test
    public void testLoginEndpoint() throws Exception {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com"); // 로그인 이메일 설정
        loginDto.setPassword("password123"); // 로그인 비밀번호 설정

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("encodedPassword"); // 암호화된 비밀번호 저장

        // Mock the behavior
        // 이메일로 유저 정보 찾으면 userDto 반환
        when(userService.findByEmail("test@example.com")).thenReturn(userDto);
        // 입력 비밀번호가 저장된 (암호화) 비밀번호와 일치하면 true 반환
        when(passwordEncoder.matches("password123", userDto.getPassword())).thenReturn(true);

        // Act & Assert
        // 실제 HTTP POST 요청처럼 시뮬레이션을 통해 /auth/login 엔드포인트 테스트
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 본문 JSON 형식
                        .content(objectMapper.writeValueAsString(loginDto)))  // loginDto 객체 JSON 으로 변환해서 요청 본문에  넣음
                .andExpect(status().isOk()) // 응답 HTTP 상태 코드 200 ok 인지 확인
                .andExpect(jsonPath("$.result").value("success")) // JSON 응답의 result 가 success 인지 확인
                .andExpect(jsonPath("$.userDto.email").value("test@example.com")); // 응답에 포함된 userDto.email 값이 맞는지 확인
    }

    @Test
    public void testLogoutEndpoint() throws Exception {
        // Act & Assert
        // 실제 HTTP POST 요청을 /auth/logout 엔드포인트로 보냄
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk()) // 응답 HTTP 상태 코드 200 ok 인지 확인
                .andExpect(content().string("logout success")); // 응답 본문 내용이 "logout success" 인지 확인
    }

    @Test
    public void testRegisterEndpoint() throws Exception {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("newuser@example.com"); //  이메일 설정
        userDto.setPassword("password123"); // 비밀번호 설정

        // Mock the behavior
        // userService.signup(userDto) 를 호출해도 실제로 아무 동작도 하지 않도록 설정
        doNothing().when(userService).signUp(userDto);

        // Act & Assert
        // /auth/register 엔드포인트에 JSON 형식으로 userDto 데이터를 POST 요청
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 본문 JSON 으로
                        .content(objectMapper.writeValueAsString(userDto))) // userDto 객체를 JOSN 문자열로 변환하여 요청 본문에 넣는다.
                .andExpect(status().isOk()) // 응답 상태 코드 200 ok 인지 확인
                .andExpect(jsonPath("$.result").value("success")) // JSON 응답 값에서 result 값이 success 인지 확인
                .andExpect(jsonPath("$.userDto.email").value("newuser@example.com")); // JSON 응답 내 userDto.email 값이 기대한 이메일 정보와 일치하는지 확인
    }
}