package com.digitalcloset.repository

import com.digitalcloset.model.Outfit
import org.springframework.data.jpa.repository.JpaRepository

interface OutfitRepository : JpaRepository<Outfit, Long> 