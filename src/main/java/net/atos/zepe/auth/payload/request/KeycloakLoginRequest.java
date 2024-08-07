package net.atos.zepe.auth.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class KeycloakLoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    private String realm;
    @JsonProperty("server_url")
    private String serverUrl;

}
