package com.study.erp.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.study.erp.model.entity.Board;

import jakarta.transaction.Transactional;

@Transactional
public interface BoardRepository extends JpaRepository<Board, Integer>, JpaSpecificationExecutor<Board>{
}
