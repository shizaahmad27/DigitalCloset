package com.digitalcloset.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

@RestController
@RequestMapping("/api/images")
class ImageController {

    private val uploadDir = Paths.get("uploads")

    init {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir)
        }
    }

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<Map<String, String>> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Please select a file to upload"))
        }

        // Validate file type
        val contentType = file.contentType
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Only image files are allowed"))
        }

        // Validate file size (max 5MB)
        if (file.size > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(mapOf("error" to "File size should be less than 5MB"))
        }

        try {
            // Generate a unique filename
            val originalFilename = file.originalFilename ?: "image"
            val extension = originalFilename.substringAfterLast('.', "")
            val filename = "${UUID.randomUUID()}.$extension"
            val filePath = uploadDir.resolve(filename)

            // Save the file
            Files.copy(file.inputStream, filePath)

            // Return the URL to access the image
            val imageUrl = "/uploads/$filename"
            return ResponseEntity.ok(mapOf("url" to imageUrl))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(mapOf("error" to "Failed to upload file: ${e.message}"))
        }
    }

    @GetMapping("/{filename}")
    fun getImage(@PathVariable filename: String): ResponseEntity<ByteArray> {
        val filePath = uploadDir.resolve(filename)
        
        return if (Files.exists(filePath)) {
            val bytes = Files.readAllBytes(filePath)
            val contentType = when {
                filename.endsWith(".jpg", ignoreCase = true) || filename.endsWith(".jpeg", ignoreCase = true) -> 
                    MediaType.IMAGE_JPEG_VALUE
                filename.endsWith(".png", ignoreCase = true) -> 
                    MediaType.IMAGE_PNG_VALUE
                filename.endsWith(".gif", ignoreCase = true) ->
                    MediaType.IMAGE_GIF_VALUE
                filename.endsWith(".webp", ignoreCase = true) ->
                    "image/webp"
                else -> MediaType.APPLICATION_OCTET_STREAM_VALUE
            }
            
            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(bytes)
        } else {
            ResponseEntity.notFound().build()
        }
    }
} 