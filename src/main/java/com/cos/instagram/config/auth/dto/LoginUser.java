package com.cos.instagram.config.auth.dto;

import com.cos.instagram.domain.user.User;

import lombok.Data;

@Data
public class LoginUser {

	private int id;
	private String username;
	private String email;
	private String role;
	private String name;
	private String provider;
	private String providerId;
	
	
	public LoginUser(User user) {
		super();
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.role = user.getRole().getKey();
		this.name = user.getName();
		this.provider = user.getProvider();
		this.providerId = user.getProviderId();
	}
	
	public User getUser() {
		return User.builder()
				.id(id)
				.build();
	}
}
