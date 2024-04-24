package com.example.trainermanager.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    private final List<Converter<?,?>> converters;

    public AppConfig(List<Converter<?,?>> converters) {
        this.converters = converters;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        converters.forEach(modelMapper::addConverter);

        return modelMapper;
    }
}
