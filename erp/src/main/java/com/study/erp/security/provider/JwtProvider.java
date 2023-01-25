package com.study.erp.security.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.study.erp.model.entity.Authority;
import com.study.erp.security.details.JpaUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtProvider {

	@Value("${jwt.secret.key}")
	private String salt;
	
	// 만료시간 : 1Hour
	@Value("${token.access.expiration}")
	private int exp;
	
	private Key secretKey;
	
	
	private final JpaUserDetailsService userDetailsService;
	
	@PostConstruct
	protected void init() {
		secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
	}
	
	// 토큰 생성
	public String createToken(String account, List<Authority> roles) {
		Claims claims = Jwts.claims().setSubject(account);
		claims.put("roles", roles);
		Date now = new Date();
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + exp))
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact()
				;
	}
	
	// 권한정보 획득
	// Spring Security 인증과정에서 권한확인을 위한 기능
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	// 토큰에 담겨있는 유저 account 획득
	public String getAccount(String token) {
		// 만료된 토큰에 대해 parseClaimsJws를 수행하면 io.jsonwebtoken.ExpiredJwtException이 발생한다.
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			return e.getClaims().getSubject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
	}

	// Authorization Header를 통해 인증을 한다.
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}
	
	// 토큰검증
	public boolean validationToken(String token) {
		try {
			// Bearer 검증
			if(!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
				return false;
			} else {
				token = token.split(" ")[1].trim();
			}
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			claims.getBody();
			// 만료되어있을 시 false
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}
