package com.digitalcloset.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService {
    private val logger = LoggerFactory.getLogger(JwtService::class.java)

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Value("\${jwt.expiration}")
    private var jwtExpiration: Long = 86400000 // 24 hours in milliseconds

    private fun getSigningKey(): Key {
        try {
            val keyBytes = secretKey.toByteArray()
            return Keys.hmacShaKeyFor(keyBytes)
        } catch (e: Exception) {
            logger.error("Error creating signing key: ${e.message}")
            throw RuntimeException("Error creating signing key", e)
        }
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(HashMap(), userDetails)
    }

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        try {
            return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.username)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact()
        } catch (e: Exception) {
            logger.error("Error generating token: ${e.message}")
            throw RuntimeException("Error generating token", e)
        }
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        try {
            val username = extractUsername(token)
            return username == userDetails.username && !isTokenExpired(token)
        } catch (e: Exception) {
            logger.error("Error validating token: ${e.message}")
            return false
        }
    }

    fun extractUsername(token: String): String {
        return extractClaim(token) { it.subject }
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { it.expiration }
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            logger.error("Error extracting claims from token: ${e.message}")
            throw RuntimeException("Error extracting claims from token", e)
        }
    }
} 