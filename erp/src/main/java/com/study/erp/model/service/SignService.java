package com.study.erp.model.service;

import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.study.erp.model.dto.SignRequestDTO;
import com.study.erp.model.dto.SignResponseDTO;
import com.study.erp.model.dto.TokenDTO;
import com.study.erp.model.entity.Authority;
import com.study.erp.model.entity.User;
import com.study.erp.model.entity.redis.Token;
import com.study.erp.model.repository.TokenRepository;
import com.study.erp.model.repository.UserRepository;
import com.study.erp.security.enums.UserRole;
import com.study.erp.security.provider.JwtProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignService {
	
	@Value("${token.access.expiration}")
	private int accessExp;
	
	@Value("${token.refresh.expiration}")
	private int refreshExp;
	
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	
	
	public SignResponseDTO login(SignRequestDTO signReqDTO) throws Exception {
		try {
			
			User user = userRepository.findById(signReqDTO.getUserId())
					.orElseThrow(() ->	new BadCredentialsException("잘못된 계정정보입니다."));
			if(passwordEncoder.matches(signReqDTO.getPassword(), user.getPassword())) {
				// refresh token 발급
				user.setRefreshToken(createRefreshToken(user));
				
				return SignResponseDTO.builder()
						.userId(user.getUserId())
						.name(user.getName())
						.email(user.getEmail())
						.nickname(user.getNickname())
						.roles(user.getRoles())
						.result("success")
						.token(TokenDTO.builder()
								.accessToken(jwtProvider.createToken(user.getUserId(), user.getRoles()))
								.refreshToken(user.getRefreshToken())
								.build())
						.build();
			}
		} catch (Exception e) {
			log.error("SignService.login error : " + e.getMessage());
		}
		return SignResponseDTO.builder()
				.result("fail")
				.build();
	}
	
	public String register(SignRequestDTO signReqDTO) throws Exception {
		String result = "fail";
		try {
			User user = User.builder()
					.userId(signReqDTO.getUserId())
					.password(passwordEncoder.encode(signReqDTO.getPassword()))
					.name(signReqDTO.getName())
					.nickname(signReqDTO.getNickname())
					.email(signReqDTO.getEmail())
					.build();
			
			user.setRoles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()));
			
			userRepository.save(user);
			
			result = "success";
		} catch (Exception e) {
			log.error("SignService.register error : " + e.getMessage());
			throw new Exception("잘못된 요청입니다.");
		}
		
		return result;
	}
	
	// Refresh Token ====================
	
	/**
	 * Refresh 토큰을 생성한다.
	 * Redis 내부에는 
	 * refreshToken:userId : tokenValue
	 * 형태로 저장
	 */
	public String createRefreshToken(User user) {
		Token token = Token.builder()
					.id(user.getUserId())
					.refreshToken(UUID.randomUUID().toString())
					.expiration(refreshExp)	// 초
					.build()
				;
		return token.getRefreshToken();
	}
	
	public Token validRefreshToken(User user, String refreshToken) throws Exception {
		Token token = tokenRepository.findByUserId(user.getUserId())
				.orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
		// 해당 유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
		if(token.getRefreshToken() == null) {
			return null;
		} else {
			// 토큰이 같은지 비교
			if(!token.getRefreshToken().equals(refreshToken)) {
				return null;
			}
			else {
				// 리프레시 토큰 만료일자가 얼마 남지 않았을 때 토큰 재 발급 (access token 기간보다 짧을 시 재발급)
				if(token.getExpiration() < accessExp) {
					token.builder()
						.refreshToken(createRefreshToken(user))
						.build();
				}
				
				return token;
			}
		}
	}
	
	public SignResponseDTO refreshAccessToken(SignRequestDTO signRequest) throws Exception {
		String userId = jwtProvider.getUserId(signRequest.getAccessToken());
		User user = userRepository.findById(userId)
					.orElseThrow(() -> new BadCredentialsException("잘못된 계정정보입니다."));
		Token refreshToken = validRefreshToken(user, signRequest.getRefreshToken());
		
		if(refreshToken != null) {
					
			return SignResponseDTO.builder()
					.token(
						TokenDTO.builder()
						.accessToken(jwtProvider.createToken(userId, user.getRoles()))
						.refreshToken(refreshToken.getRefreshToken())
						.build()
					)
					.result("success")
					.build();
		} else {
			throw new Exception("로그인을 해주세요");
		}
	}
}
