package com.digitalcloset.controller

import com.digitalcloset.model.Outfit
import com.digitalcloset.service.OutfitService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/outfits")
class OutfitController(private val outfitService: OutfitService) {

    @GetMapping
    fun getAll(): List<Outfit> = outfitService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Outfit = outfitService.getById(id)

    @PostMapping
    fun create(@RequestBody outfit: Outfit): Outfit = outfitService.create(outfit)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody outfit: Outfit): Outfit = outfitService.update(id, outfit)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = outfitService.delete(id)
} 