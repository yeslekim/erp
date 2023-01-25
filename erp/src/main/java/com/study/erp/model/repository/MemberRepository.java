package com.study.erp.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.erp.model.entity.Member;

import jakarta.transaction.Transactional;

@Transactional
public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByAccount(String account);
}
