package com.digitalcloset.controller

import com.digitalcloset.model.ClothingItem
import com.digitalcloset.model.ClothingCategory
import com.digitalcloset.model.EventType
import com.digitalcloset.service.ClothingItemService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/clothing-items")
class ClothingItemController(private val clothingItemService: ClothingItemService) {

    @GetMapping
    fun getAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") direction: String
    ): Page<ClothingItem> {
        val sort = Sort.by(Sort.Direction.fromString(direction), sortBy)
        return clothingItemService.getAll(PageRequest.of(page, size, sort))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ClothingItem = clothingItemService.getById(id)

    @GetMapping("/category/{category}")
    fun getByCategory(
        @PathVariable category: ClothingCategory,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") direction: String
    ): Page<ClothingItem> {
        val sort = Sort.by(Sort.Direction.fromString(direction), sortBy)
        return clothingItemService.getByCategory(category, PageRequest.of(page, size, sort))
    }

    @GetMapping("/event-type/{eventType}")
    fun getByEventType(
        @PathVariable eventType: EventType,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") direction: String
    ): Page<ClothingItem> {
        val sort = Sort.by(Sort.Direction.fromString(direction), sortBy)
        return clothingItemService.getByEventType(eventType, PageRequest.of(page, size, sort))
    }

    @GetMapping("/favorites")
    fun getFavorites(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") direction: String
    ): Page<ClothingItem> {
        val sort = Sort.by(Sort.Direction.fromString(direction), sortBy)
        return clothingItemService.getFavorites(PageRequest.of(page, size, sort))
    }

    @GetMapping("/search")
    fun searchByName(
        @RequestParam name: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") direction: String
    ): Page<ClothingItem> {
        val sort = Sort.by(Sort.Direction.fromString(direction), sortBy)
        return clothingItemService.searchByName(name, PageRequest.of(page, size, sort))
    }

    @PostMapping
    fun create(@RequestBody clothingItem: ClothingItem): ClothingItem = clothingItemService.create(clothingItem)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody clothingItem: ClothingItem): ClothingItem =
        clothingItemService.update(id, clothingItem)

    @PatchMapping("/{id}/favorite")
    fun toggleFavorite(@PathVariable id: Long): ClothingItem = clothingItemService.toggleFavorite(id)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = clothingItemService.delete(id)
} 