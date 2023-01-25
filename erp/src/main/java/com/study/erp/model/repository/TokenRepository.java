package com.study.erp.model.repository;

import org.springframework.data.repository.CrudRepository;

import com.study.erp.model.entity.redis.Token;

public interface TokenRepository extends CrudRepository<Token, Long>{

}
