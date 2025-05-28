package com.digitalcloset.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "outfits")
data class Outfit(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,
    var description: String? = null,
    var items: String? = null, // Can be JSON or comma-separated for now
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) 