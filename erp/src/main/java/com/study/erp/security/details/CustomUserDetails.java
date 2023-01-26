package com.study.erp.security.details;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.study.erp.model.entity.User;

public class CustomUserDetails implements UserDetails{

	private final User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}

	public final User getUser() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return Stream.of(user.getRoles()).map(o -> new SimpleGrantedAuthority(
//				o.getValue()
//		)).collect(Collectors.toList());
		return user.getRoles().stream().map(o -> new SimpleGrantedAuthority(
				o.getName()
		)).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserId();
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
