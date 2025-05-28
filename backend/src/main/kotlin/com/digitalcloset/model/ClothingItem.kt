package com.digitalcloset.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "clothing_items")
data class ClothingItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    val id: Long? = null,

    @Column(nullable = false)
    @JsonProperty("name")
    var name: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("category")
    var category: ClothingCategory = ClothingCategory.TOP,

    @Column(nullable = false)
    @JsonProperty("season")
    var season: String = "",

    @Column(nullable = true)
    @JsonProperty("description")
    var description: String? = null,

    @Column(nullable = true)
    @JsonProperty("brand")
    var brand: String? = null,

    @Column(name = "image_url", nullable = true)
    @JsonProperty("imageUrl")
    var imageUrl: String? = null,

    @Column(name = "is_favorite", nullable = false)
    @JsonProperty("isFavorite")
    var isFavorite: Boolean = false,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "image_id")
    @JsonProperty("image")
    var image: Image? = null,

    @ElementCollection
    @CollectionTable(name = "clothing_item_event_types", joinColumns = [JoinColumn(name = "clothing_item_id")])
    @Column(name = "event_type", columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    @JsonProperty("eventTypes")
    var eventTypes: MutableSet<EventType> = mutableSetOf(),

    @ElementCollection
    @CollectionTable(name = "clothing_item_tags", joinColumns = [JoinColumn(name = "clothing_item_id")])
    @Column(name = "tag")
    @JsonProperty("tags")
    var tags: MutableSet<String> = mutableSetOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty("createdAt")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    @JsonProperty("updatedAt")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PrePersist
    @PreUpdate
    fun updateTimestamp() {
        updatedAt = LocalDateTime.now()
    }
}

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
