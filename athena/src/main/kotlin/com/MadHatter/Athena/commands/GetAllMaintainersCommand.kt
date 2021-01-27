package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.models.MaintainerPojo
import com.MadHatter.Athena.repositories.MaintainerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetAllMaintainersCommand {

  @Autowired
  lateinit var maintainerRepository: MaintainerRepository

  fun execute(): List<MaintainerPojo> {
    maintainerRepository.findAll().also { unsorted ->
      unsorted.sortedWith(Maintainer.Sort).also { sorted ->
        return sorted.map { maintainer: Maintainer ->
          MaintainerPojo(maintainer)
        }
      }
    }
  }
}