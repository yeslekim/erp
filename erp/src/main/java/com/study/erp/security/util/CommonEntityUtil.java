package com.study.erp.security.util;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@SuperBuilder	// 부모 객체를 상속받는 자식 객체를 만들 때, 부모 객체의 필드값도 지정할 수 있게 하기 위해서 사용
@NoArgsConstructor
public class CommonEntityUtil {

	private LocalDateTime regDt;
	private String regId;
	private LocalDateTime modDt;
	private String modId;
	private String delYn;
	
	@PrePersist // 데이터 생성이 이루어질때 사전 작업
	public void prePersist() {
		this.regDt = LocalDateTime.now();
		this.modDt = this.regDt;
		this.regId = SecurityUtil.getUserId();
		this.modId = SecurityUtil.getUserId();
	}
	
	@PreUpdate // 데이터 수정이 이루어질때 사전 작업
	public void preUpdate() {
		this.modDt = LocalDateTime.now();
		this.modId = SecurityUtil.getUserId();
	}
}
