package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.Standby
import com.MadHatter.Athena.repositories.StandbyRepository
import com.MadHatter.Athena.repositories.MaintainerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CreateStandbyCommand {
  @Autowired
  lateinit var maintainerRepository: MaintainerRepository

  @Autowired
  lateinit var standbyRepository: StandbyRepository

  fun execute(startDate: String, endDate: String, maintainerId: Long) {
    val maintainer = maintainerRepository.findById(maintainerId).get()

    val newStandby = Standby(LocalDate.parse(startDate),
      LocalDate.parse(endDate),
      maintainer,
      null)

    standbyRepository.save(newStandby)
  }
}