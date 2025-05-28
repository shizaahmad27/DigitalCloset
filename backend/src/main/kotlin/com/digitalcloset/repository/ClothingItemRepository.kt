package com.digitalcloset.repository

import com.digitalcloset.model.ClothingItem
import com.digitalcloset.model.ClothingCategory
import com.digitalcloset.model.EventType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClothingItemRepository : JpaRepository<ClothingItem, Long> {
    fun findByCategory(category: ClothingCategory, pageable: Pageable): Page<ClothingItem>
    fun findByEventTypesContaining(eventType: EventType, pageable: Pageable): Page<ClothingItem>
    fun findByIsFavoriteTrue(pageable: Pageable): Page<ClothingItem>
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<ClothingItem>
} 