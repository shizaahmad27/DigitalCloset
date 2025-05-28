package com.digitalcloset.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "clothing_items")
data class ClothingItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var imageUrl: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var category: ClothingCategory,

    @ElementCollection
    @CollectionTable(name = "clothing_item_event_types", joinColumns = [JoinColumn(name = "clothing_item_id")])
    @Column(name = "event_type")
    var eventTypes: MutableSet<EventType> = mutableSetOf(),

    var brand: String? = null,
    var season: String? = null,
    var isFavorite: Boolean = false,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class ClothingCategory {
    TOP,
    BOTTOM,
    DRESS,
    OUTERWEAR,
    SHOES,
    ACCESSORY,
    OUTFIT
}

enum class EventType {
    DAY,
    NIGHT,
    DATE,
    CASUAL,
    BEACH,
    SCHOOL,
    FORMAL,
    SPORTS,
    WORK
} 