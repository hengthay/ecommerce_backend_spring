package com.hengthay.ecommercebackendspring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

// Config on WebMvc to handling client request
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Storing upload path directory
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // To get the path directory and convert it to full file system path
        // like C:/projects/ecommerce
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        // Map incoming HTTP URLs (/uploads/**) to that physical folder
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
