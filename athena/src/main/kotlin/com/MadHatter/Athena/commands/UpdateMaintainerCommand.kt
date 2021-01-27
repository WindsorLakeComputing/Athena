package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.*
import com.MadHatter.Athena.repositories.CertificateRepository
import com.MadHatter.Athena.repositories.MaintainerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UpdateMaintainerCommand {

  @Autowired
  lateinit var maintainerRepository: MaintainerRepository

  @Autowired
  lateinit var certificateRepository: CertificateRepository

  fun execute(maintainer: MaintainerPojo) {
    val foundMaintainer = maintainerRepository.getOne(maintainer.id)

    foundMaintainer.certificates = locateMaintainerCerts(maintainer)

    maintainerRepository.save(foundMaintainer)
  }

  private fun locateMaintainerCerts(maintainer: MaintainerPojo): List<Certificate> {
    return maintainer.certificates.map { cert ->
      certificateRepository.getByName(cert.name).first()
    }
  }
}
