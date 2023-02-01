package com.study.erp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.erp.model.dto.UserResponseDTO;
import com.study.erp.model.service.UserService;
import com.study.erp.security.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;

	@GetMapping("/get")
	public ResponseEntity<UserResponseDTO> getUser() throws Exception {
		return new ResponseEntity<>(userService.getUser(SecurityUtil.getUserId()), HttpStatus.OK);
	}
}
