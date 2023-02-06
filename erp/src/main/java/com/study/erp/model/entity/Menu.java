package com.study.erp.model.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.study.erp.security.util.CommonEntityUtil;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor 
@NoArgsConstructor
@DynamicInsert	// insert시 지정된 defualt값을 적용
@DynamicUpdate	// 수정된 데이터만 update
public class Menu extends CommonEntityUtil{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer menuId;
	
	@NotNull
	@Column
	private String menuNm;
	
	@NotNull
	@Column
	private Integer menuOrder;

}
