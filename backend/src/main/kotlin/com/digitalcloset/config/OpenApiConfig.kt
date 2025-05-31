package com.digitalcloset.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class OpenApiConfig {
    @Bean
    open fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Digital Closet API")
                    .description("API for Digital Closet application")
                    .version("1.0")
                    .contact(
                        Contact()
                            .name("Digital Closet Team")
                    )
            )
    }
} 