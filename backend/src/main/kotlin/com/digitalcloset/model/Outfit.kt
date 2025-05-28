package com.digitalcloset.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "outfits")
data class Outfit(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,           // READ-ONLY - ID skal ikke endres
    var name: String,           // MUTABLE - navn kan endres
    var description: String? = null, // MUTABLE - beskrivelse kan endres
    var items: String? = null,  // MUTABLE - items kan endres
    val createdAt: LocalDateTime = LocalDateTime.now(), // READ-ONLY - opprettelsestid skal ikke endres
    var updatedAt: LocalDateTime = LocalDateTime.now()  // MUTABLE - oppdateringstid skal endres
)