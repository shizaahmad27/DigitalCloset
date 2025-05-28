package com.digitalcloset.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "images")
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val fileName: String,

    @Column(nullable = false)
    val originalFileName: String,

    @Column(nullable = false)
    val mimeType: String,

    @Column(nullable = false)
    val size: Long,

    @Column(nullable = false)
    val path: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToOne(mappedBy = "image")
    var clothingItem: ClothingItem? = null
) 