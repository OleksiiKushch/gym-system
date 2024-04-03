package org.example.config.storage.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.example.constants.GeneralConstants.CREATE_HIBERNATE_SESSION_FACTORY_EXCEPTION_MSG;

@Slf4j
@Configuration
public class HibernateConfig {

    private static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";

    @Bean
    public SessionFactory hibernateSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure(HIBERNATE_CFG_XML).build();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception exception) {
            StandardServiceRegistryBuilder.destroy(registry);
            log.error(CREATE_HIBERNATE_SESSION_FACTORY_EXCEPTION_MSG, exception);
            throw new ExceptionInInitializerError(exception);
        }
    }
}
