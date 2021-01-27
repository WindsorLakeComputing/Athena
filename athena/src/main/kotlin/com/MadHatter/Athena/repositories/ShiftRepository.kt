package com.MadHatter.Athena.repositories

import com.MadHatter.Athena.models.Shift
import org.springframework.data.jpa.repository.JpaRepository

interface ShiftRepository : JpaRepository<Shift, Long> {
  fun getByName(name: String): List<Shift>
}