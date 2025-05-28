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
open class ClothingItemService(private val repository: ClothingItemRepository) {

    @Transactional(readOnly = true)
    open fun getAll(pageable: Pageable): Page<ClothingItem> {
        return repository.findAll(pageable)
    }

    @Transactional(readOnly = true)
    open fun getById(id: Long): ClothingItem {
        return repository.findById(id).orElseThrow { NoSuchElementException("Clothing item not found with id: $id") }
    }

    @Transactional
    open fun create(item: ClothingItem): ClothingItem {
        return repository.save(item)
    }

    @Transactional
    open fun update(id: Long, updatedItem: ClothingItem): ClothingItem {
        val existingItem = getById(id)
        
        existingItem.apply {
            name = updatedItem.name
            imageUrl = updatedItem.imageUrl
            category = updatedItem.category
            eventTypes = updatedItem.eventTypes
            brand = updatedItem.brand
            season = updatedItem.season
            description = updatedItem.description
            tags = updatedItem.tags
            updatedAt = java.time.LocalDateTime.now()
        }
        
        return repository.save(existingItem)
    }

    @Transactional
    open fun delete(id: Long) {
        if (!repository.existsById(id)) {
            throw NoSuchElementException("Clothing item not found with id: $id")
        }
        repository.deleteById(id)
    }

    @Transactional
    open fun toggleFavorite(id: Long): ClothingItem {
        val item = getById(id)
        item.isFavorite = !item.isFavorite
        return repository.save(item)
    }

    @Transactional(readOnly = true)
    open fun getByCategory(category: ClothingCategory, pageable: Pageable): Page<ClothingItem> {
        return repository.findByCategory(category, pageable)
    }

    @Transactional(readOnly = true)
    open fun getByEventType(eventType: EventType, pageable: Pageable): Page<ClothingItem> {
        return repository.findByEventTypesContaining(eventType, pageable)
    }

    @Transactional(readOnly = true)
    open fun searchByName(name: String, pageable: Pageable): Page<ClothingItem> {
        return repository.findByNameContainingIgnoreCase(name, pageable)
    }

    @Transactional(readOnly = true)
    open fun getByTag(tag: String, pageable: Pageable): Page<ClothingItem> {
        return repository.findByTagsContaining(tag, pageable)
    }
} 