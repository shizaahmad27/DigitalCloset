package com.digitalcloset.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

@RestController
@RequestMapping("/api/upload")
@Tag(name = "File Upload", description = "APIs for handling file uploads")
class FileUploadController {

    private val uploadDir = Paths.get("uploads").toAbsolutePath()

    init {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir)
        }
    }

    @PostMapping("/image")
    @Operation(summary = "Upload an image file")
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<Map<String, String>> {
        try {
            if (file.isEmpty) {
                return ResponseEntity.badRequest().body(mapOf("error" to "File is empty"))
            }

            if (!file.contentType?.startsWith("image/")!!) {
                return ResponseEntity.badRequest().body(mapOf("error" to "File must be an image"))
            }

            val fileName = "${UUID.randomUUID()}.${file.originalFilename?.substringAfterLast('.')}"
            val filePath = uploadDir.resolve(fileName)
            
            file.transferTo(filePath.toFile())
            
            return ResponseEntity.ok(mapOf(
                "url" to "/uploads/$fileName"
            ))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.internalServerError().body(mapOf("error" to "Failed to upload file: ${e.message}"))
        }
    }
} 