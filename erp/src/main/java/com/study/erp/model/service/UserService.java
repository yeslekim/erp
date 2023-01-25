package com.study.erp.model.service;

import org.springframework.stereotype.Service;

import com.study.erp.model.dto.UserResponseDTO;
import com.study.erp.model.entity.Member;
import com.study.erp.model.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
	
	private final MemberRepository memberRepository;

	public UserResponseDTO getMember(String account) throws Exception {
		Member member = memberRepository.findByAccount(account)
				.orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
		return new UserResponseDTO(member);
	}
}
