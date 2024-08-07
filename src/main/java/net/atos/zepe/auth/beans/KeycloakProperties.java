package net.atos.zepe.auth.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

    private String serverUrl;
    private String realm;
    private String  clientId;
    private String clientSecret;
    private String client;
    private Http http;

    @Getter
    @Setter
    public static class Http {
        private int poolSize = 5;
        private int socketTimeout = -1;
        private int establishConnectionTimeout = -1;

        private int connectionCheckoutTimeout = -1;

        private Proxy proxy;

        @Setter
        @Getter
        public static class Proxy {
            private boolean enable = false;
            private String host = "proxy";
            private int port = 3128;
        }
    }
}
