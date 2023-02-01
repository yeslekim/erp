package com.study.erp.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.study.erp.model.entity.Authority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder 
@AllArgsConstructor 
@NoArgsConstructor
public class SignResponseDTO extends CommonDTO{
	
	private Long id;

	private String userId;

	private String nickname;

	private String name;

	private String email;

	private List<Authority> roles = new ArrayList<>();

	private TokenDTO token;

}
