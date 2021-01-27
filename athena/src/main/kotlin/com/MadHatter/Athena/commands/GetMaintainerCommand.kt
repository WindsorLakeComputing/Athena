package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.MaintainerPojo
import com.MadHatter.Athena.repositories.MaintainerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetMaintainerCommand {

  @Autowired
  lateinit var maintainerRepository: MaintainerRepository

  fun execute(maintainerId: Long) : MaintainerPojo? =
    maintainerRepository.findById(maintainerId).let {
      return when (it.isPresent) {
        true -> MaintainerPojo(it.get())
        false -> null
      }
    }
}