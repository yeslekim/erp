package com.study.erp.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder 
@AllArgsConstructor 
@NoArgsConstructor
@DynamicInsert	// insert시 지정된 defualt값을 적용
@DynamicUpdate	// 수정된 데이터만 update
public class User {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private String userId;
	
	@Column
	private String password;
	
	@Column
	private String nickname;
	
	@Column
	private String name;
	
	@Column(unique = true)
	private String email;
	
	@Column
	private String refreshToken;
	
	@Column
	private LocalDateTime createDt;
	
	@Column
	private String delYn;
	
	@PrePersist // 데이터 생성이 이루어질때 사전 작업
	public void prePersist() {
		this.createDt = LocalDateTime.now();
	}
	
	/*
	 * 1. @OneToMany : 1:N의 관계
	 * 
	 * 2. fetch 	1) EAGER 	: 관계된 Entity의 정보를 미리 읽어옴
	 * 				2) LAZE		: 관계된 Entity의 정보를 요청하는 순간 읽어옴
	 * 
	 * 3. mappedBy : 양방향 관계의 연관관계 주인이 아님을 증명
	 * 				 @OneToMany(Authority에서는 @ManyToOne)으로 양방향 관계가 되었으나 DB입장을 고려하여 추가 설정이 필요
	 * 				 외래키를 갖는 쪽, 즉 연관관계의 주인(1:N인경우 N이 주인)이 아닌 것을 표현하는 설정
	 */
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Builder.Default
	private List<Authority> roles = new ArrayList<>();
	
	public void setRoles(List<Authority> role) {
		this.roles = role;
		role.forEach(o -> o.setUser(this));
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
