package com.study.erp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor 
@NoArgsConstructor
public class SignRequestDTO {

	private Long id;
	
	private String userId;
	
	private String password;
	
	private String nickname;
	
	private String name;
	
	private String email;
	
	private String accessToken;
	
	private String refreshToken;
	
}
