package net.atos.zepe.auth.payload.request;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;

public class KeycloakTokenRequest {

    @NotBlank
    private String realm;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
