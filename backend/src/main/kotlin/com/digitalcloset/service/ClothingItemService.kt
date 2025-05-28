package com.digitalcloset.service

import com.digitalcloset.model.ClothingItem
import com.digitalcloset.model.ClothingCategory
import com.digitalcloset.model.EventType
import com.digitalcloset.repository.ClothingItemRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ClothingItemService(private val repository: ClothingItemRepository) {

    fun getAll(pageable: Pageable): Page<ClothingItem> = repository.findAll(pageable)

    fun getById(id: Long): ClothingItem = repository.findById(id)
        .orElseThrow { NoSuchElementException("Clothing item not found with id: $id") }

    fun getByCategory(category: ClothingCategory, pageable: Pageable): Page<ClothingItem> = 
        repository.findByCategory(category, pageable)

    fun getByEventType(eventType: EventType, pageable: Pageable): Page<ClothingItem> = 
        repository.findByEventTypesContaining(eventType, pageable)

    fun getFavorites(pageable: Pageable): Page<ClothingItem> = 
        repository.findByIsFavoriteTrue(pageable)

    fun searchByName(name: String, pageable: Pageable): Page<ClothingItem> =
        repository.findByNameContainingIgnoreCase(name, pageable)

    @Transactional
    fun create(clothingItem: ClothingItem): ClothingItem {
        clothingItem.createdAt = LocalDateTime.now()
        clothingItem.updatedAt = LocalDateTime.now()
        return repository.save(clothingItem)
    }

    @Transactional
    fun update(id: Long, clothingItem: ClothingItem): ClothingItem {
        val existingItem = getById(id)
        
        existingItem.apply {
            name = clothingItem.name
            imageUrl = clothingItem.imageUrl
            category = clothingItem.category
            eventTypes = clothingItem.eventTypes
            brand = clothingItem.brand
            season = clothingItem.season
            isFavorite = clothingItem.isFavorite
            updatedAt = LocalDateTime.now()
        }

        return repository.save(existingItem)
    }

    @Transactional
    fun toggleFavorite(id: Long): ClothingItem {
        val item = getById(id)
        item.isFavorite = !item.isFavorite
        item.updatedAt = LocalDateTime.now()
        return repository.save(item)
    }

    @Transactional
    fun delete(id: Long) = repository.deleteById(id)
} 