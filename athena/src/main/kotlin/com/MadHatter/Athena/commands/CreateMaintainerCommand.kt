package com.MadHatter.Athena.commands

import com.MadHatter.Athena.controllers.webModels.NewMaintainer
import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.repositories.MaintainerRepository
import com.MadHatter.Athena.repositories.SectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreateMaintainerCommand {
  @Autowired
  lateinit var maintainerRepository: MaintainerRepository

  @Autowired
  lateinit var sectionRepository: SectionRepository

  fun execute(maintainer: NewMaintainer) =
    Maintainer(
      firstName = maintainer.firstName,
      lastName = maintainer.lastName,
      //dummy values for the rest of the fields
      employeeId = "0",
      level = "1",
      rank = "0",
      section = sectionRepository.getByName("APG").first())
      .also {
        maintainerRepository.save(it)
      }
}