package com.study.erp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.study.erp.model.dto.SignRequestDTO;
import com.study.erp.model.dto.SignResponseDTO;
import com.study.erp.model.entity.Authority;
import com.study.erp.model.entity.User;
import com.study.erp.model.repository.UserRepository;
import com.study.erp.model.service.SignService;
import com.study.erp.security.enums.UserRole;
import com.study.erp.security.provider.JwtProvider;

import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SignServiceTest {
	
	@Spy
	@InjectMocks
	private SignService target;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Mock
	JwtProvider jwtProvider;

	SignRequestDTO signReqDTO = new SignRequestDTO();

	private String userId;
	
	@BeforeEach
	void setUp() throws ServletException {
		userId = "test";
		signReqDTO = SignRequestDTO.builder()
				.userId(userId)
				.password("test")
				.name("test")
				.nickname("testNick")
				.email("test@test.com")
				.build()
				;
	}
	
	@Test
	@DisplayName("login 테스트")
	public void login_test() throws Exception {
		// given
		Optional<User> user = Optional.of(User.builder()
				.userId(userId)
				.name("test")
				.email("test@abc.com")
				.roles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()))
				.build())
				;
		doReturn(user).when(userRepository).findById(anyString());
		doReturn(true).when(passwordEncoder).matches(any(), any());
		doReturn(userId).when(target).createRefreshToken(any());
		doReturn(userId).when(jwtProvider).createToken(any(), any());
		
		// when
		final SignResponseDTO result = target.login(signReqDTO);
		
		// then
		assertThat(result.getResult()).isEqualTo("success");
	}
	
	@Test
	@DisplayName("회원가입 테스트")
	public void register_test() throws Exception {
		// given
		User user = User.builder()
				.userId(userId)
				.name("test")
				.email("test@abc.com")
				.roles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()))
				.build()
				;
		doReturn(user).when(userRepository).save(any());
		
		// when
		final SignResponseDTO result = target.register(signReqDTO);
		
		// then
		assertThat(result.getResult()).isEqualTo("success");
	}
}
