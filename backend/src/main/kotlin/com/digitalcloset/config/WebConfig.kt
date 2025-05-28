package com.digitalcloset.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths

@Configuration
open class WebConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val uploadDir = Paths.get("uploads").toAbsolutePath().toString()
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:$uploadDir/")
    }
} 