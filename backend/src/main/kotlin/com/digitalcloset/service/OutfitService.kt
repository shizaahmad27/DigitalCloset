package com.digitalcloset.service

import com.digitalcloset.model.Outfit
import com.digitalcloset.repository.OutfitRepository
import org.springframework.stereotype.Service

@Service
class OutfitService(private val outfitRepository: OutfitRepository) {
    fun getAll(): List<Outfit> = outfitRepository.findAll()
    fun getById(id: Long): Outfit = outfitRepository.findById(id).orElseThrow { RuntimeException("Outfit not found") }
    fun create(outfit: Outfit): Outfit = outfitRepository.save(outfit)
    fun update(id: Long, updated: Outfit): Outfit {
        val existing = getById(id)
        return outfitRepository.save(existing.copy(
            name = updated.name,
            description = updated.description,
            items = updated.items,
            updatedAt = java.time.LocalDateTime.now()
        ))
    }
    fun delete(id: Long) = outfitRepository.deleteById(id)
} 