package com.digitalcloset.service

import com.digitalcloset.model.Outfit
import com.digitalcloset.repository.OutfitRepository
import com.digitalcloset.repository.ClothingItemRepository
import com.digitalcloset.controller.OutfitController.OutfitRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OutfitService(
    private val outfitRepository: OutfitRepository,
    private val clothingItemRepository: ClothingItemRepository
) {
    fun getAll(): List<Outfit> = outfitRepository.findAll()
    fun getById(id: Long): Outfit = outfitRepository.findById(id).orElseThrow { RuntimeException("Outfit not found") }
    fun create(outfit: Outfit): Outfit = outfitRepository.save(outfit)
    fun update(id: Long, updated: Outfit): Outfit {
        val existing = getById(id)
        return outfitRepository.save(existing.copy(
            name = updated.name,
            description = updated.description,
            items = updated.items,
            updatedAt = LocalDateTime.now()
        ))
    }
    fun delete(id: Long) = outfitRepository.deleteById(id)

    fun createFromRequest(req: OutfitRequest): Outfit {
        val items = clothingItemRepository.findAllById(req.itemIds).toMutableSet()
        val outfit = Outfit(
            name = req.name,
            description = req.description,
            items = items
        )
        return outfitRepository.save(outfit)
    }

    fun updateFromRequest(id: Long, req: OutfitRequest): Outfit {
        val existing = getById(id)
        val items = clothingItemRepository.findAllById(req.itemIds).toMutableSet()
        return outfitRepository.save(existing.copy(
            name = req.name,
            description = req.description,
            items = items,
            updatedAt = LocalDateTime.now()
        ))
    }
} 