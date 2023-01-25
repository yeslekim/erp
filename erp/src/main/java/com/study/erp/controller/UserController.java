package com.study.erp.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.erp.model.dto.SignResponseDTO;
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
		return new ResponseEntity<>(userService.getMember(SecurityUtil.getAccount()), HttpStatus.OK);
	}
	
	@GetMapping("/test")
	@ResponseBody
	public SignResponseDTO test(@RequestParam Map<String, Object> input) throws Exception {
		return SignResponseDTO.builder()
				.result("success")
				.account("tset")
				.build();
	}
}
