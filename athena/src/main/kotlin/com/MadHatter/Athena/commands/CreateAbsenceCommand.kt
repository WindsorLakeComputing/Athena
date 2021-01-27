package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.Absence
import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.repositories.MaintainerRepository
import com.MadHatter.Athena.repositories.AbsenceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class CreateAbsenceCommand {
  @Autowired
  lateinit var maintainerRepository: MaintainerRepository

  @Autowired
  lateinit var absenceRepository: AbsenceRepository

  fun execute(startDate: String, endDate: String, hours: String, reason: String, location: String, maintainerId: Long) {
    maintainerRepository.findById(maintainerId).let {
      val newAbsence = Absence(LocalDate.parse(startDate),
        LocalDate.parse(endDate),
        it.get(),
        reason,
        location,
        hours)

      absenceRepository.save(newAbsence)
    }
  }
}
