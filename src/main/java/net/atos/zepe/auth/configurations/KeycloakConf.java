package net.atos.zepe.auth.configurations;

import net.atos.zepe.auth.beans.KeycloakProperties;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
public class KeycloakConf {
    private final KeycloakProperties properties;

    public KeycloakConf(KeycloakProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Keycloak keycloak() {
        KeycloakBuilder keycloakBuilder = KeycloakBuilder.builder()
                .serverUrl(properties.getServerUrl())
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret());

        if (Objects.nonNull(properties.getHttp())) {
            ResteasyClientBuilderImpl resteasyClientBuilder = new ResteasyClientBuilderImpl();
                    resteasyClientBuilder
                    .connectionPoolSize(properties.getHttp().getPoolSize())
                    .readTimeout(properties.getHttp().getSocketTimeout(), TimeUnit.MILLISECONDS)
                    .connectTimeout(properties.getHttp().getEstablishConnectionTimeout(), TimeUnit.MILLISECONDS)
                    .connectionCheckoutTimeout(properties.getHttp().getConnectionCheckoutTimeout(), TimeUnit.MILLISECONDS);


            if (Objects.nonNull(properties.getHttp().getProxy()) && properties.getHttp().getProxy().isEnable()) {
                resteasyClientBuilder.defaultProxy(properties.getHttp().getProxy().getHost(), properties.getHttp().getProxy().getPort());
            }
            keycloakBuilder.resteasyClient(resteasyClientBuilder.build());
        }


        return keycloakBuilder.build();
    }
}
