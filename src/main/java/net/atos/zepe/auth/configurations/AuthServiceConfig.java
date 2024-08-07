package net.atos.zepe.auth.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"net.atos.zepe.auth.models"})
@EnableJpaRepositories(basePackages = {"net.atos.zepe.auth.repository"})
public class AuthServiceConfig {

    @Bean
    public OpenAPI openAPI() {
        Contact contact = new Contact();
        contact.setEmail("contact@zepe.com");
        contact.setName("Zepe Auth");
        contact.setUrl("https://www.zepe.com");
        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Authentication Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage authentications.")
                .termsOfService("https://www.zepe.com/terms")
                .license(mitLicense);

        return new OpenAPI().info(info);
    }
}
