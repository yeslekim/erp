package com.study.erp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.study.erp.model.dto.UserResponseDTO;
import com.study.erp.model.entity.Authority;
import com.study.erp.model.entity.User;
import com.study.erp.model.repository.UserRepository;
import com.study.erp.model.service.UserService;
import com.study.erp.security.enums.UserRole;

@ExtendWith(MockitoExtension.class)
@Transactional
public class UserServiceTest {
	
	@Spy
	@InjectMocks
	private UserService target;
	
	@Mock
	private UserRepository userRepository;
	
	@Test
	@DisplayName("회원정보 조회 테스트")
	public void getUser_test() throws Exception {
		// given
		String userId = "test";
		Optional<User> user = Optional.of(User.builder()
				.userId(userId)
				.name("test")
				.email("test@abc.com")
				.roles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()))
				.build())
				;
		UserResponseDTO userResDTO = new UserResponseDTO().builder()
										.userId(userId)
										.name("test")
										.email("test@abc.com")
										.roles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()))
										.build();
									
		doReturn(user).when(userRepository).findById(anyString());
		
		// when
		final UserResponseDTO result = target.getUser(userId);
		
		// then
		assertThat(result.getUserId()).isEqualTo(userResDTO.getUserId());
		assertThat(result.getName()).isEqualTo(userResDTO.getName());
		assertThat(result.getEmail()).isEqualTo(userResDTO.getEmail());
		assertThat(result.getRoles().get(0).getName()).isEqualTo(userResDTO.getRoles().get(0).getName());
	}
}
