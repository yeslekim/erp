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
public class CommonDTO {
	
	private String result;	// success or fail
	
	private String regDt;
	private String regId;
	private String regDtSearchFrom; // 최초등록일 검색용
	private String regDtSearchTo; 	// 최초등록일 검색용
	
	private String modDt;
	private String modId;
	private String modDtSearchFrom; // 최종수정일 검색용
	private String modDtSearchTo; 	// 최종수정일 검색용
	
}
