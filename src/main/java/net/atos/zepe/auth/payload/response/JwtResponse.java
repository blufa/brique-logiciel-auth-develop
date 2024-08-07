package net.atos.zepe.auth.payload.response;

import net.atos.zepe.auth.models.UserEntity;

public class JwtResponse {
	private UserEntity user;
	private String token;
	public JwtResponse(String token, UserEntity user) {
		this.user = user;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
}
