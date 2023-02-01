package com.study.erp.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.study.erp.model.dto.BoardRequestDTO;
import com.study.erp.model.dto.BoardResponseDTO;
import com.study.erp.model.entity.Board;
import com.study.erp.model.repository.BoardRepository;
import com.study.erp.model.specification.BoardSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

	private final BoardRepository boardRepository;
	
	public Page<BoardResponseDTO> getBoardList(Pageable pageable, BoardRequestDTO boardReqDTO) throws Exception {
		// 게시판 리스트 출력
		return chgBoardDTO(boardRepository.findAll(BoardSpecification.searchWith(boardReqDTO), pageable));
	}
	
	public BoardResponseDTO addBoard(BoardResponseDTO boardReqDTO) throws Exception {
		// 게시판 등록
		BoardResponseDTO boardResDTO = new BoardResponseDTO();
		String result = "fail";
		try {
			Board board = Board.builder()
					.boardType(boardReqDTO.getBoardType())
					.title(boardReqDTO.getTitle())
					.contents(boardReqDTO.getContents())
					.regId(boardReqDTO.getRegId())
					.modId(boardReqDTO.getModId())
					.build();
			boardResDTO = new BoardResponseDTO(boardRepository.save(board));
			result = "success";
		} catch (Exception e) {
			log.error("BoardService.addBoard error : " + e.getMessage());
		}
		boardResDTO.setResult(result);
		return boardResDTO;
	}
	
	public BoardResponseDTO editBoard(BoardResponseDTO boardReqDTO) throws Exception {
		// 게시판 등록
		BoardResponseDTO boardResDTO = new BoardResponseDTO();
		String result = "fail";
		try {
			Board board = Board.builder()
					.boardType(boardReqDTO.getBoardType())
					.title(boardReqDTO.getTitle())
					.contents(boardReqDTO.getContents())
					.regId(boardReqDTO.getRegId())
					.modId(boardReqDTO.getModId())
					.build();
			boardResDTO = new BoardResponseDTO(boardRepository.save(board));
			result = "success";
		} catch (Exception e) {
			log.error("BoardService.editBoard error : " + e.getMessage());
		}
		boardResDTO.setResult(result);
		return boardResDTO;
	}
	
	// Page<Entity> -> Page<Dto> 변환처리
	public Page<BoardResponseDTO> chgBoardDTO(Page<Board> boardList){
		return boardList.map(m -> BoardResponseDTO.builder()
				.boardId(m.getBoardId())
				.boardType(m.getBoardType())
				.title(m.getTitle())
				.contents(m.getContents())
				.regDt(String.valueOf(m.getRegDt()))
				.regId(m.getRegId())
				.modDt(String.valueOf(m.getModDt()))
				.modId(m.getModId())
				.build());
	}
}
