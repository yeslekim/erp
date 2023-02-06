package com.study.erp.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.erp.model.dto.UserResponseDTO;
import com.study.erp.model.entity.Authority;
import com.study.erp.model.service.UserService;
import com.study.erp.security.enums.UserRole;
import com.study.erp.security.provider.JwtProvider;

import jakarta.servlet.ServletException;

@SpringBootTest
public class UserControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	UserService userService;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() throws ServletException {
		// security filter 추가
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}
	
	public String toJsonString(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}
	
	@Test
	@DisplayName("유저정보조회 테스트")
	public void getUser_test() throws Exception {
		// given
		UserResponseDTO userResDTO = new UserResponseDTO().builder()
								.userId("test")
								.build();
		doReturn(userResDTO).when(userService).getUser(anyString());
		
		String token = "BEARER " + jwtProvider.createToken("test", Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()));
		assertNotNull(token);
		
		//when
		ResultActions actions = mockMvc.perform(get("/user/get")
				.header("Authorization", token));
				
		// then
		actions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(toJsonString(userResDTO)))
			;
	}
}
