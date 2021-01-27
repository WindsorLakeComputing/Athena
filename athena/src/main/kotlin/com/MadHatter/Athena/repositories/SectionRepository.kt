package com.MadHatter.Athena.repositories

import com.MadHatter.Athena.models.Section
import org.springframework.data.jpa.repository.JpaRepository

interface SectionRepository : JpaRepository<Section, Long> {
  fun getByName(name: String): List<Section>
}