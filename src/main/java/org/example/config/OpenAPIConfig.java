package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

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
