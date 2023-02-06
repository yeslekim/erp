package com.study.erp.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.erp.model.dto.BoardRequestDTO;
import com.study.erp.model.dto.BoardResponseDTO;
import com.study.erp.model.entity.Authority;
import com.study.erp.model.service.BoardService;
import com.study.erp.security.enums.UserRole;
import com.study.erp.security.provider.JwtProvider;

import jakarta.servlet.ServletException;

@SpringBootTest
public class BoardControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	BoardService boardService;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private String token;
	
	BoardRequestDTO boardReqDTO = new BoardRequestDTO();
	BoardResponseDTO boardResDTO = new BoardResponseDTO();
	
	@BeforeEach
	void setUp() throws ServletException {
		// security filter 추가
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
		
		token = "BEARER " + jwtProvider.createToken("test", Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()));
		assertNotNull(token);
	}
	
	public String toJsonString(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}
	
	@Test
	@DisplayName("게시판 리스트 테스트")
	public void boardList_test() throws Exception {
		// given
		List<BoardResponseDTO> boardList = new ArrayList<>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<BoardResponseDTO> boardResDTO = new PageImpl<>(boardList, pageable, 0);
		
		doReturn(boardResDTO).when(boardService).getBoardList(any(), any());
		
		// when
		ResultActions actions = mockMvc.perform(get("/user/board/list")
				.header("Authorization", token));
		
		// then
		actions
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(toJsonString(boardResDTO)))
		;
	}
	
	@Test
	@DisplayName("게시판 글작성 테스트")
	public void boardAdd_test() throws Exception {
		// given
		doReturn(boardResDTO).when(boardService).addBoard(any());
		
		// when
		ResultActions actions = mockMvc.perform(post("/user/board/add")
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(boardReqDTO)));
		
		// then
		actions
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(toJsonString(boardResDTO)))
		;
	}
	
	@Test
	@DisplayName("게시판 글수정 테스트")
	public void boardEdit_test() throws Exception {
		// given
		doReturn(boardResDTO).when(boardService).editBoard(any());
		
		// when
		ResultActions actions = mockMvc.perform(patch("/user/board/edit")
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(boardReqDTO)));
		
		// then
		actions
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(toJsonString(boardResDTO)))
		;
	}
}
