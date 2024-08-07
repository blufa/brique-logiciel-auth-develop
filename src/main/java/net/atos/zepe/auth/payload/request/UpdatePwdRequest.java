package net.atos.zepe.auth.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdatePwdRequest {
	@NotBlank
	private String keycloakIdentifier;

	@NotBlank
	private String userName;
	@NotBlank
	private String realm;

	@NotBlank
	@Size(min = 5, max = 20)
	private String newPassword;

	@Size(min = 5, max = 20)
	private String oldPassword;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Email
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getKeycloakIdentifier() {
		return keycloakIdentifier;
	}

	public void setKeycloakIdentifier(String keycloakIdentifier) {
		this.keycloakIdentifier = keycloakIdentifier;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

}
