package com.digitalcloset

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class DigitalClosetApplication

fun main(args: Array<String>) {
    runApplication<DigitalClosetApplication>(*args)
} 