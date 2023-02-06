package com.study.erp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.erp.model.dto.BoardRequestDTO;
import com.study.erp.model.dto.BoardResponseDTO;
import com.study.erp.model.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/board")
public class BoardController {
	
	private final BoardService boardService;

	//Pageable 파라미터 형식: ?page=2&size=3&sort=id,desc&sort=username,desc
	@GetMapping("/list")
	public ResponseEntity<Page<BoardResponseDTO>> boardList(BoardRequestDTO boardReqDTO, Pageable pageable) throws Exception {
		return new ResponseEntity<>(boardService.getBoardList(pageable, boardReqDTO) , HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<BoardResponseDTO> boardAdd(@RequestBody BoardRequestDTO boardReqDTO) throws Exception {
		return new ResponseEntity<>(boardService.addBoard(boardReqDTO) , HttpStatus.OK);
	}
	@PatchMapping("/edit")
	public ResponseEntity<BoardResponseDTO> boardEdit(@RequestBody BoardRequestDTO boardReqDTO) throws Exception {
		return new ResponseEntity<>(boardService.editBoard(boardReqDTO) , HttpStatus.OK);
	}
}
