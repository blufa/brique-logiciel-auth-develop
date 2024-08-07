package net.atos.zepe.auth.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakHttpProperties {

    private Integer poolSize;
    private Integer socketTimeout;
    private Integer establishConnectionTimeout;

    private Integer connectionCheckoutTimeout;

    private KeycloakHttpProxyProperties proxy;

}
