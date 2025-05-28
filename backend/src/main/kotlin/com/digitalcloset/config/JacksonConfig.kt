package com.digitalcloset.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class JacksonConfig {
    @Bean
    open fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
            configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        }
    }
} 