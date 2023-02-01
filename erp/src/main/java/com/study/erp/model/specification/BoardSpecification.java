package com.study.erp.model.specification;

import java.util.ArrayList;
import java.util.List;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.study.erp.model.dto.BoardRequestDTO;
import com.study.erp.model.entity.Board;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/*
 * 게시판 검색조건
 */
@Component
public class BoardSpecification {

	public static Specification<Board> searchWith(BoardRequestDTO request) {
		return (Specification<Board>) ((root, query, builder) -> {
			List<Predicate> predicate = getPredicateWithRequest(request, root, builder);
			return builder.and(predicate.toArray(new Predicate[0]));
		});
	}
	
	private static List<Predicate> getPredicateWithRequest(BoardRequestDTO request
			, Root<Board> root, CriteriaBuilder builder) {
		List<Predicate> predicate = new ArrayList<>();
		// 게시판 타입
		if(!StringUtils.isEmpty(request.getBoardType())) {
			predicate.add(builder.like(root.get("boardType"), "%"+request.getBoardType()+"%"));
		}
		// 게시판 제목
		if(!StringUtils.isEmpty(request.getTitle())) {
			predicate.add(builder.like(root.get("title"), "%"+request.getTitle()+"%"));
		}
		// 게시판 내용
		if(!StringUtils.isEmpty(request.getContents())) {
			predicate.add(builder.like(root.get("contents"), "%"+request.getContents()+"%"));
		}
		// 작성일 검색 시작일
		if(!StringUtils.isEmpty(request.getRegDtSearchFrom())) {
			predicate.add(builder.greaterThanOrEqualTo(root.get("regDt"), request.getRegDtSearchFrom()));
		}
		// 작성일 검색 마지막일
		if(!StringUtils.isEmpty(request.getRegDtSearchTo())) {
			predicate.add(builder.lessThanOrEqualTo(root.get("regDt"), request.getRegDtSearchTo()));
		}
		return predicate;
	}
}
