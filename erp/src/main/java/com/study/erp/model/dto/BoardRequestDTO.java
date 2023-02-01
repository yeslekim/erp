package com.study.erp.model.dto;

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
public class BoardRequestDTO extends CommonDTO{

	private String boardId;
	
	private String boardType;
	
	private String title;
	
	private String contents;
	
	private String regDt;
	
	private String regId;
	
	private String modDt;
	
	private String modId;
	
}
