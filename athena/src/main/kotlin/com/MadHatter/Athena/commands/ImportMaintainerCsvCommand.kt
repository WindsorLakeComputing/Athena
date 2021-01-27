package com.MadHatter.Athena.commands

import com.MadHatter.Athena.controllers.webModels.UserName
import com.MadHatter.Athena.models.Certificate
import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.models.Shift
import com.MadHatter.Athena.repositories.CertificateRepository
import com.MadHatter.Athena.repositories.MaintainerRepository
import com.MadHatter.Athena.repositories.SectionRepository
import com.MadHatter.Athena.repositories.ShiftRepository
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalArgumentException

private val logger = KotlinLogging.logger {}

@Service
class ImportMaintainerCsvCommand {

  @Autowired
  lateinit var maintainerRepository: MaintainerRepository

  @Autowired
  lateinit var shiftRepository: ShiftRepository

  @Autowired
  lateinit var certificateRepository: CertificateRepository

  @Autowired
  lateinit var sectionRepository: SectionRepository

  @Autowired
  lateinit var userNameSessionCommand: UserNameSessionCommandInterface

  fun execute(file: MultipartFile): String? {

    fun getUsername(): UserName = userNameSessionCommand.execute()
    logger.info("Maintainer CSV Upload by " + getUsername())

    if (file.isEmpty) {
      logger.error("Uploaded maintainer CSV empty")
      return "No maintainers created (File Empty)."
    }

      val inputStream = file.inputStream
      val fileReader = BufferedReader(InputStreamReader(inputStream))

    val records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(fileReader)

    try {
      val unsavedMaintainers = records
        .groupBy { record -> getMaintainerKey(record) }
        .values
        .map { list -> createOrUpdateMaintainer(list) }
      maintainerRepository.saveAll(unsavedMaintainers)
    } catch (e: IllegalArgumentException) {
      logger.error("No maintainers created: " + e.localizedMessage)
      return "No maintainers created (${e.localizedMessage})."
    } finally {
      fileReader.close()
    }

    return null
  }

  private fun createOrUpdateMaintainer(recordsForMaintainer: List<CSVRecord>): Maintainer {
    val templateRecord = recordsForMaintainer[0]

    val courseCodesList = recordsForMaintainer.map { record ->
      record.get("courseCode")
    }

    val certsList: List<Certificate> = getCertificates(courseCodesList)

    val foundMaintainer = maintainerRepository.getByFirstNameAndLastNameAndEmployeeId(
      templateRecord.get("firstName"),
      templateRecord.get("lastName"),
      templateRecord.get("employeeNumber")
    )

    if (foundMaintainer != null) {
      val existingCerts = foundMaintainer.certificates
      foundMaintainer.certificates = existingCerts.union(certsList).toList()

      return foundMaintainer
    } else {
      return Maintainer(
        templateRecord.get("firstName"),
        templateRecord.get("lastName"),
        templateRecord.get("employeeNumber"),
        certsList,
        getShiftEntity(templateRecord.get("firstPreference")),
        getShiftEntity(templateRecord.get("secondPreference")),
        getShiftEntity(templateRecord.get("thirdPreference")),
        templateRecord.get("level"),
        templateRecord.get("rank"),
        sectionRepository.getByName((templateRecord.get("section"))).first(),
        getShiftEntity(templateRecord.get("assignedShift")),
        mutableListOf(),
        listOf()
      )
    }
  }

  private fun getShiftEntity(shift: String?): Shift? {
    return when (shift) {
      "D" -> shiftRepository.getByName("D").first()
      "S" -> shiftRepository.getByName("S").first()
      "M" -> shiftRepository.getByName("M").first()
      else -> null
    }
  }

  private fun getCertificates(courseCodes: List<String>): List<Certificate> =
    CourseToCertTranslator.getCerts(courseCodes)
      .map { certString -> loadOrCreateCertificates(certString) }


  private fun loadOrCreateCertificates(certName: String): Certificate {
    val cert = certificateRepository.getByName(certName)
    if (cert.isNotEmpty()) {
      return cert.first()
    }

    return certificateRepository.save(Certificate(certName))
  }

  private fun getMaintainerKey(record: CSVRecord): String =
    "${record.get("firstName")}${record.get("lastName")}${record.get("employeeNumber")}"
}
