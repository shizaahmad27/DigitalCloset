package com.digitalcloset.service

import com.digitalcloset.dto.AuthResponse
import com.digitalcloset.dto.LoginRequest
import com.digitalcloset.dto.SignupRequest
import com.digitalcloset.exception.UserAlreadyExistsException
import com.digitalcloset.model.User
import com.digitalcloset.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun signup(request: SignupRequest): AuthResponse {
        logger.info("Attempting signup for email: ${request.email}")
        
        try {
            // Check if username exists
            if (userRepository.existsByUsername(request.username)) {
                logger.warn("Signup failed - username already exists: ${request.username}")
                throw UserAlreadyExistsException("Username '${request.username}' is already taken")
            }

            // Check if email exists
            if (userRepository.existsByEmail(request.email)) {
                logger.warn("Signup failed - email already exists: ${request.email}")
                throw UserAlreadyExistsException("Email '${request.email}' is already registered")
            }

            // Create new user
            val user = User(
                firstname = request.firstname,
                lastname = request.lastname,
                username = request.username,
                email = request.email,
                password = request.password // Store password as plain text for development
            )
            userRepository.save(user)
            logger.info("User created successfully: ${user.email}")

            // Generate token
            val userDetails = userDetailsService.loadUserByUsername(user.email ?: throw IllegalStateException("Email cannot be null"))
            val token = jwtService.generateToken(userDetails)
            logger.info("Token generated successfully for user: ${user.email}")

            return AuthResponse(
                token = token,
                firstname = user.firstname ?: "",
                lastname = user.lastname ?: "",
                username = user.username ?: "",
                email = user.email ?: ""
            )
        } catch (e: Exception) {
            logger.error("Error during signup: ${e.message}", e)
            throw e
        }
    }

    fun login(request: LoginRequest): AuthResponse {
        logger.info("Attempting login for email: ${request.email}")
        
        try {
            // Authenticate user
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
            logger.info("Authentication successful for email: ${request.email}")

            // Load user and generate token
            val userDetails = userDetailsService.loadUserByUsername(request.email)
            val token = jwtService.generateToken(userDetails)
            logger.info("Token generated successfully for email: ${request.email}")

            // Get user from database
            val user = userRepository.findByEmail(request.email)
                ?: throw UsernameNotFoundException("User not found with email: ${request.email}")
            logger.info("User found in database: ${user.email}")

            return AuthResponse(
                token = token,
                firstname = user.firstname ?: "",
                lastname = user.lastname ?: "",
                username = user.username ?: "",
                email = user.email ?: ""
            )
        } catch (e: BadCredentialsException) {
            logger.warn("Login failed - invalid credentials for email: ${request.email}")
            throw e
        } catch (e: UsernameNotFoundException) {
            logger.warn("Login failed - user not found: ${request.email}")
            throw e
        } catch (e: Exception) {
            logger.error("Error during login: ${e.message}", e)
            throw e
        }
    }
} 