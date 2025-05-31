package com.digitalcloset.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotBlank
    val firstname: String? = null,

    @NotBlank
    val lastname: String? = null,

    @NotBlank
    @Column(unique = true)
    val username: String? = null,

    @NotBlank
    @Email
    @Column(unique = true)
    val email: String? = null,

    @NotBlank
    val password: String? = null,

    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER
)

enum class UserRole {
    USER,
    ADMIN
} 