package com.study.erp.model.entity.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash("refreshToken")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

	@Id
	@JsonIgnore
	private String id;
	
	private String refreshToken;
	
	@TimeToLive(unit = TimeUnit.SECONDS)
	private Integer expiration;
	
	public void setExpiration(Integer expiration) {
		this.expiration = expiration;
	}
}
