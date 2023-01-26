package com.study.erp.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.study.erp.model.entity.Authority;
import com.study.erp.model.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder 
@AllArgsConstructor 
@NoArgsConstructor
public class UserResponseDTO {

	private String userId;

	private String nickname;

	private String name;

	private String email;

	private List<Authority> roles = new ArrayList<>();

	private TokenDTO token;
	
	private String result;
	
	public UserResponseDTO(User user) {
		this.userId = user.getUserId();
		this.nickname = user.getNickname();
		this.name = user.getName();
		this.email = user.getEmail();
		this.roles = user.getRoles();
	}
}
