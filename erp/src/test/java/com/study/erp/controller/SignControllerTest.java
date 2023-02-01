package com.study.erp.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.erp.model.dto.SignRequestDTO;
import com.study.erp.model.dto.SignResponseDTO;
import com.study.erp.model.dto.TokenDTO;
import com.study.erp.model.entity.Authority;
import com.study.erp.model.service.SignService;
import com.study.erp.security.enums.UserRole;

import jakarta.servlet.ServletException;

@SpringBootTest
public class SignControllerTest {
	
	private MockMvc mockMvc;
	
	@MockBean
	private SignService signService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private WebApplicationContext context;
	
	@BeforeEach
	void setUp() throws ServletException {
		// security filter 추가
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
		signReqDTO = SignRequestDTO.builder()
				.userId("mockTest")
				.password("test")
				.nickname("testNick")
				.email("test@test.com")
				.build()
				;
	}
	
	public String toJsonString(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}
	
	SignRequestDTO signReqDTO = new SignRequestDTO();
	
	SignResponseDTO signResDTO = new SignResponseDTO();
	
	@Test
	@DisplayName("회원가입 테스트")
	public void register_test() throws Exception{
		// given
		String result = "success";
		
		doReturn(result).when(signService).register(any());
		
		// when
		ResultActions actions = mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(signReqDTO)));
		
		// then
		actions
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().string(containsString("success")))
			;
	}
	
	@Test
	@DisplayName("로그인 테스트")
	public void login_test() throws Exception{
		// given
		signResDTO = SignResponseDTO.builder()
				.id(signReqDTO.getId())
				.userId(signReqDTO.getUserId())
				.name(signReqDTO.getName())
				.email(signReqDTO.getEmail())
				.roles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()))
				.result("success")
				.token(TokenDTO.builder()
						.accessToken("access")
						.refreshToken("refresh")
						.build())
				.build();
		
		doReturn(signResDTO).when(signService).login(any());
		
		// when
		ResultActions actions = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(signReqDTO)));
		
		// then
		actions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(toJsonString(signResDTO)))
			.andExpect(content().string(containsString("success")))
			;
		
	}
}
