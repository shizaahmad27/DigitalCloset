package com.digitalcloset.controller

import com.digitalcloset.model.Outfit
import com.digitalcloset.service.OutfitService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/outfits")
class OutfitController(private val outfitService: OutfitService) {

    data class OutfitRequest(
        val name: String,
        val description: String? = null,
        val itemIds: List<Long> = emptyList()
    )

    @GetMapping
    fun getAll(): List<Outfit> = outfitService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Outfit = outfitService.getById(id)

    @PostMapping
    fun create(@RequestBody req: OutfitRequest): Outfit = outfitService.createFromRequest(req)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: OutfitRequest): Outfit = outfitService.updateFromRequest(id, req)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = outfitService.delete(id)
} 