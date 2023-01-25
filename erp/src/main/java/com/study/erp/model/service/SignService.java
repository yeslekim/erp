package com.study.erp.model.service;

import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.erp.model.dto.SignRequestDTO;
import com.study.erp.model.dto.SignResponseDTO;
import com.study.erp.model.dto.TokenDTO;
import com.study.erp.model.entity.Authority;
import com.study.erp.model.entity.Member;
import com.study.erp.model.entity.redis.Token;
import com.study.erp.model.repository.MemberRepository;
import com.study.erp.model.repository.TokenRepository;
import com.study.erp.security.enums.UserRole;
import com.study.erp.security.provider.JwtProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SignService {
	
	@Value("${token.access.expiration}")
	private int accessExp;
	
	@Value("${token.refresh.expiration}")
	private int refreshExp;
	
	private final MemberRepository memberRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	
	
	public SignResponseDTO login(SignRequestDTO request) throws Exception {
		Member member = memberRepository.findByAccount(request.getAccount())
				.orElseThrow(() ->	new BadCredentialsException("잘못된 계정정보입니다."));
		
		if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			return SignResponseDTO.builder()
					.result("fail")
					.build();
					
		}
		
		// refresh token 발급
		member.setRefreshToken(createRefreshToken(member));
		
		return SignResponseDTO.builder()
				.id(member.getId())
				.account(member.getAccount())
				.name(member.getName())
				.email(member.getEmail())
				.nickname(member.getNickname())
				.roles(member.getRoles())
				.result("success")
				.token(TokenDTO.builder()
						.access_token(jwtProvider.createToken(member.getAccount(), member.getRoles()))
						.refresh_token(member.getRefreshToken())
						.build())
				.build()
				;
	}
	
	public String register(SignRequestDTO request) throws Exception {
		String result = "fail";
		try {
			Member member = Member.builder()
					.account(request.getAccount())
					.password(passwordEncoder.encode(request.getPassword()))
					.name(request.getName())
					.nickname(request.getNickname())
					.email(request.getEmail())
//					.roles(UserRole.USER)
					.build();
			
			member.setRoles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()));
			
			memberRepository.save(member);
			
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
	 * refreshToken:memberId : tokenValue
	 * 형태로 저장
	 */
	public String createRefreshToken(Member member) {
		Token token = Token.builder()
					.id(member.getId())
					.refresh_token(UUID.randomUUID().toString())
					.expiration(refreshExp)	// 초
					.build()
				;
		return token.getRefresh_token();
	}
	
	public Token validRefreshToken(Member member, String refreshToken) throws Exception {
		Token token = tokenRepository.findById(member.getId())
				.orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
		// 해당 유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
		if(token.getRefresh_token() == null) {
			return null;
		} else {
			// 토큰이 같은지 비교
			if(!token.getRefresh_token().equals(refreshToken)) {
				return null;
			}
			else {
				// 리프레시 토큰 만료일자가 얼마 남지 않았을 때 토큰 재 발급 (access token 기간보다 짧을 시 재발급)
				if(token.getExpiration() < accessExp) {
					token.builder()
						.refresh_token(createRefreshToken(member))
						.build();
				}
				
				return token;
			}
		}
	}
	
	public SignResponseDTO refreshAccessToken(SignRequestDTO signRequest) throws Exception {
		String account = jwtProvider.getAccount(signRequest.getAccess_token());
		Member member = memberRepository.findByAccount(account)
					.orElseThrow(() -> new BadCredentialsException("잘못된 계정정보입니다."));
		Token refreshToken = validRefreshToken(member, signRequest.getRefresh_token());
		
		if(refreshToken != null) {
					
			return SignResponseDTO.builder()
					.token(
						TokenDTO.builder()
						.access_token(jwtProvider.createToken(account, member.getRoles()))
						.refresh_token(refreshToken.getRefresh_token())
						.build()
					)
					.result("success")
					.build();
		} else {
			throw new Exception("로그인을 해주세요");
		}
	}
}
