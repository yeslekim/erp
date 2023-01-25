package com.study.erp.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
	// authority에는 꼭 ROLE_*과 같은 형태로 저장해야한다.
	// spring security에서 권한을 다룰 때 자체적으로 PREFIX로 ROLE_을 추가하기 때문이다.
	
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String value;
}
