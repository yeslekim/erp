package com.study.erp.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.study.erp.model.entity.Authority;
import com.study.erp.model.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder 
@AllArgsConstructor 
@NoArgsConstructor
public class UserResponseDTO {

	private Long id;

	private String account;

	private String nickname;

	private String name;

	private String email;

	private List<Authority> roles = new ArrayList<>();

	private TokenDTO token;
	
	private String result;
	
	public UserResponseDTO(Member member) {
		this.id = member.getId();
		this.account = member.getAccount();
		this.nickname = member.getNickname();
		this.name = member.getName();
		this.email = member.getEmail();
		this.roles = member.getRoles();
	}
}
