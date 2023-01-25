package com.study.erp.security.details;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.study.erp.model.entity.Member;

public class CustomUserDetails implements UserDetails{

	private final Member member;

	public CustomUserDetails(Member member) {
		this.member = member;
	}

	public final Member getMember() {
		return member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return Stream.of(member.getRoles()).map(o -> new SimpleGrantedAuthority(
//				o.getValue()
//		)).collect(Collectors.toList());
		return member.getRoles().stream().map(o -> new SimpleGrantedAuthority(
				o.getName()
		)).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getAccount();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
