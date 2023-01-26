package com.study.erp.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.study.erp.model.entity.Authority;

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
public class SignResponseDTO {
	
	private Long id;

	private String userId;

	private String nickname;

	private String name;

	private String email;

	private List<Authority> roles = new ArrayList<>();

	private TokenDTO token;
	
	private String result;

}
