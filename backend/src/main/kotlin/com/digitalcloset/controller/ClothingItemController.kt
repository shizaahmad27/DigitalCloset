package com.digitalcloset.controller

import com.digitalcloset.model.ClothingItem
import com.digitalcloset.model.ClothingCategory
import com.digitalcloset.model.EventType
import com.digitalcloset.service.ClothingItemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/clothing-items")
@Tag(name = "Clothing Items", description = "APIs for managing clothing items")
class ClothingItemController(private val service: ClothingItemService) {

    @GetMapping
    @Operation(summary = "Get all clothing items with pagination")
    fun getAll(
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") size: Int,
        @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") sortBy: String,
        @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(defaultValue = "DESC") direction: String
    ): ResponseEntity<Page<ClothingItem>> {
        val pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortBy)
        return ResponseEntity.ok(service.getAll(pageable))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a clothing item by ID")
    fun getById(@PathVariable id: Long): ResponseEntity<ClothingItem> {
        return ResponseEntity.ok(service.getById(id))
    }

    @PostMapping
    @Operation(summary = "Create a new clothing item")
    fun create(@RequestBody item: ClothingItem): ResponseEntity<ClothingItem> {
        return ResponseEntity.ok(service.create(item))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing clothing item")
    fun update(
        @PathVariable id: Long,
        @RequestBody item: ClothingItem
    ): ResponseEntity<ClothingItem> {
        return ResponseEntity.ok(service.update(id, item))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a clothing item")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        service.delete(id)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{id}/favorite")
    @Operation(summary = "Toggle favorite status of a clothing item")
    fun toggleFavorite(@PathVariable id: Long): ResponseEntity<ClothingItem> {
        return ResponseEntity.ok(service.toggleFavorite(id))
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get clothing items by category")
    fun getByCategory(
        @PathVariable category: ClothingCategory,
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ClothingItem>> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return ResponseEntity.ok(service.getByCategory(category, pageable))
    }

    @GetMapping("/event-type/{eventType}")
    @Operation(summary = "Get clothing items by event type")
    fun getByEventType(
        @PathVariable eventType: EventType,
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ClothingItem>> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return ResponseEntity.ok(service.getByEventType(eventType, pageable))
    }

    @GetMapping("/search")
    @Operation(summary = "Search clothing items by name")
    fun searchByName(
        @Parameter(description = "Search query") @RequestParam query: String,
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ClothingItem>> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return ResponseEntity.ok(service.searchByName(query, pageable))
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Get clothing items by tag")
    fun getByTag(
        @PathVariable tag: String,
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ClothingItem>> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return ResponseEntity.ok(service.getByTag(tag, pageable))
    }
} 