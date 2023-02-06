package com.study.erp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.erp.model.dto.SignRequestDTO;
import com.study.erp.model.dto.SignResponseDTO;
import com.study.erp.model.service.SignService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SignController {
	
	private final SignService signService;
	
	@PostMapping(value = "/register")
	public ResponseEntity<SignResponseDTO> signup(@RequestBody SignRequestDTO signRequest) throws Exception {
		return new ResponseEntity<>(signService.register(signRequest), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<SignResponseDTO> signin(@RequestBody SignRequestDTO signRequest) throws Exception {
		SignResponseDTO s = signService.login(signRequest);
		return new ResponseEntity<>(s, HttpStatus.OK);
	}
	
//	@GetMapping("/refresh")
//	public ResponseEntity<SignResponseDTO> refresh(@RequestBody SignRequestDTO signRequest) throws Exception {
//		// front에서 access 토큰이 만료되었을 시 refresh토큰을 확인 후 access토큰을 재발급해줌
//		return new ResponseEntity<>(signService.refreshAccessToken(signRequest) , HttpStatus.OK);
//	}
	
}
