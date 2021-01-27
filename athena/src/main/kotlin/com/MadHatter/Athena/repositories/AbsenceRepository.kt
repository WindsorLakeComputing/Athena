package com.MadHatter.Athena.repositories

import com.MadHatter.Athena.models.Absence
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AbsenceRepository : JpaRepository<Absence, Long> {
  fun findByEndDateGreaterThanEqualAndStartDateLessThanEqual(date: LocalDate, date2: LocalDate): List<Absence>

  fun findByMaintainerId(maintainerID: Long): List<Absence>
}
