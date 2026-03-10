package mx.utvt.evaluaciondocente.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${app.openapi.title:API de Evaluacion Docente}")
    private String title;

    @Value("${app.openapi.version:1.0}")
    private String version;

    @Value("${app.openapi.description:Sistema de evaluacion academica UTVT}")
    private String description;

    @Value("${app.openapi.contact.name:IDGS-84}")
    private String contactName;

    @Value("${app.openapi.contact.email:idgs84@utvt.edu.mx}")
    private String contactEmail;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .contact(new Contact()
                                .name(contactName)
                                .email(contactEmail)));
    }
}
