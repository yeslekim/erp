package com.study.erp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.study.erp.model.dto.BoardRequestDTO;
import com.study.erp.model.dto.BoardResponseDTO;
import com.study.erp.model.entity.Board;
import com.study.erp.model.repository.BoardRepository;
import com.study.erp.model.service.BoardService;

@ExtendWith(MockitoExtension.class)
@Transactional
public class BoardServiceTest {
	
	@Spy
	@InjectMocks
	private BoardService target;
	
	@Mock
	private BoardRepository boardRepository;
	
	BoardRequestDTO boardReqDTO = new BoardRequestDTO();
	BoardResponseDTO boardResDTO = new BoardResponseDTO();
	
	@Test
	@DisplayName("게시판 리스트 테스트")
	public void getBoardList_test() throws Exception {
		// given
		List<BoardResponseDTO> boardList = new ArrayList<>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<BoardResponseDTO> boardResDTO = new PageImpl<>(boardList, pageable, 0);
		
		doReturn(boardResDTO).when(target).chgBoardDTO(any());
		
		// when
		final Page<BoardResponseDTO> result = target.getBoardList(pageable, boardReqDTO);
		
		// then
		assertThat(result).isNotNull();
	}
	
	@Test
	@DisplayName("게시판 등록 테스트")
	public void boardAdd_test() throws Exception {
		// given
		Board board = new Board();
		doReturn(board).when(boardRepository).save(any());
		
		// when
		final BoardResponseDTO result = target.addBoard(boardReqDTO);
		
		// then 
		assertThat(result.getResult()).isEqualTo("success");
	}
	
	@Test
	@DisplayName("게시판 수정 테스트")
	public void boardEdit_test() throws Exception {
		// given
		Board board = new Board();
		doReturn(board).when(boardRepository).save(any());
		
		// when
		final BoardResponseDTO result = target.addBoard(boardReqDTO);
		
		// then 
		assertThat(result.getResult()).isEqualTo("success");
	}

}
