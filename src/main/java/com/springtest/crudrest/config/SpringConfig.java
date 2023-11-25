package com.springtest.crudrest.config;

import com.springtest.crudrest.converters.GendersConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new GendersConverter());
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}