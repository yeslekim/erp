package com.study.erp.security.config;

import java.io.IOException;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.study.erp.security.filter.JwtAuthenticationFilter;
import com.study.erp.security.provider.JwtProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class JwtSecurityConfig {

	private final JwtProvider jwtProvider;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// ID, Password 문자열을 Base64로 인코딩하여 전달하는 구조
			.httpBasic().disable()
			// 쿠키 기반이 아닌 JWT 기반이므로 사용하지 않음
			.csrf().disable()
			// CORS 설정
			.cors().configurationSource(corsConfigurationSource())
			.and()
			// Spring Security 세션 정책 : 세션 생성 및 사용하지 않음
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			// 조건별로 요청 허용/제한 설정
			.authorizeHttpRequests()
			// 정적 자원 접근 허용
			.requestMatchers("/static/**").permitAll()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
			// home 승인
			.requestMatchers("/", "/view/**").permitAll()
			// 회원가입과 로그인은 모두 승인
			.requestMatchers("/register", "/login", "/refresh").permitAll()
			// /admin으로 시작하는 요청은 ADMIN 권한이 있는 유저에게만 허용
			.requestMatchers("/admin/**").hasRole("ADMIN")
			// /user로 시작하는 요청은 USER 권한이 있는 유저에게만 허용
//			.requestMatchers("/user/**").hasRole("USER")
//			.anyRequest().denyAll()
			.anyRequest().permitAll()
			.and()
			// JWT 인증 필터 적용
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
			// 에러핸들링
			.exceptionHandling()
			.accessDeniedHandler(new AccessDeniedHandler() {
				
				@Override
				public void handle(HttpServletRequest request, HttpServletResponse response,
						AccessDeniedException accessDeniedException) throws IOException, ServletException {
					// 권한 문제가 발생했을 때 이 부분을 호출한다.
					response.setStatus(403);
					response.setCharacterEncoding("utf-8");
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().write("권한이 없는 사용자입니다.");
					
//					response.sendRedirect(request.getHeader("Referer"));
				}
			})
			.authenticationEntryPoint(new AuthenticationEntryPoint() {
				
				@Override
				public void commence(HttpServletRequest request, HttpServletResponse response,
						AuthenticationException authException) throws IOException, ServletException {
					// 인증문제가 발생했을 때 이 부분을 호출한다.
					response.setStatus(401);
					response.setCharacterEncoding("utf-8");
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().write("인증되지 않은 사용자입니다.");
					
					response.sendRedirect("/");
				}
			});
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// PasswordEncoder를 createDelegatingPasswordEncoder()로 설정하면
		// {noop} asdf!@#asdfvz!@#... 처럼 password의 앞에 Encoding 방식이 붙은채로 저장되어 암호화 방식을 지정하여 저장할 수 있다.
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*");	// 허용할 URL
		configuration.addAllowedMethod("*");	// 허용할 Method
//		configuration.addAllowedHeader("*");	// 허용할 Http Method
		
		configuration.addExposedHeader("X-AUTH-TOKEN");	// exposedHeader에 설정을 해야 헤더값이 제대로 반환됨
		
		configuration.setAllowCredentials(true);
		
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
