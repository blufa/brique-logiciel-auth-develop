package net.atos.zepe.auth.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakHttpProxyProperties {
    private boolean enable;
    private String host;
    private Integer port;
}
