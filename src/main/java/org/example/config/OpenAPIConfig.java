package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocUIConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SpringDocConfiguration.class,
        SwaggerConfig.class,
        SwaggerUiConfigProperties.class,
        SwaggerUiOAuthProperties.class,
        SpringDocUIConfiguration.class,
        SpringDocWebMvcConfiguration.class,
        JacksonAutoConfiguration.class,
})
public class OpenAPIConfig {

    @Bean
    public SpringDocConfigProperties springDocConfigProperties() {
        return new SpringDocConfigProperties();
    }

    @Bean
    public OpenAPI ApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gym System API")
                        .description("""
                                Application for training management in a gym.
                                
                                In the system, there are two main types of users, namely Trainee and Trainer.
                                
                                Users are able to register their profiles, modify profile information and activate or\s
                                deactivate profiles. The system also provides specific functionality depending on the\s
                                type of user, for example, Trainees have the option to select one or more trainers.\s
                                Some operations for trainees and trainers require login credentials.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DEV team / GitLab repository")
                                .email("Oleksii_Kushch@epam.com")
                                .url("https://git.epam.com/oleksii_kushch/gym-system"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                );
    }
}
