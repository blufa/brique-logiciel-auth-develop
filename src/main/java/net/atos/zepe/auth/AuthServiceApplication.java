package net.atos.zepe.auth;

import net.atos.zepe.auth.beans.KeycloakProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;

@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(KeycloakProperties.class)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}


	@PostConstruct
	private void onInit() {
		System.out.println("+++++++++++++++++++++++++++++++++");
		System.getenv().forEach((key, value) -> System.out.println(key + " --> " + value));
		System.out.println("+++++++++++++++++++++++++++++++++");
	}
}
