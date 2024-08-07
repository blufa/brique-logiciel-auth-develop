package net.atos.zepe.auth.beans;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CustomKeycloak {

    private final KeycloakProperties properties;

    public CustomKeycloak(KeycloakProperties properties) {
        this.properties = properties;
    }

    public Keycloak keycloakWith(String username, String password) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getServerUrl())
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .build();
    }
}
