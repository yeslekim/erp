package com.study.erp.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.erp.model.entity.User;

import jakarta.transaction.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, String>{
}
