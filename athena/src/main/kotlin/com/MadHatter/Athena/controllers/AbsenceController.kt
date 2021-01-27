package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.controllers.webModels.AbsencesWebModel
import com.MadHatter.Athena.controllers.webModels.NewAbsence
import com.MadHatter.Athena.models.Absence
import com.MadHatter.Athena.models.AbsencePojo
import com.MadHatter.Athena.repositories.AbsenceRepository
import com.MadHatter.Athena.commands.CreateAbsenceCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/absences")
class AbsenceController {

  @Autowired
  lateinit var absenceRepository: AbsenceRepository

  @Autowired
  lateinit var createAbsenceCommand: CreateAbsenceCommand

  @PreAuthorize( "#oauth2.hasScope('madhatter.entity.read')")
  @GetMapping(value = [""], produces = ["application/json"])
  fun getAbsencesForRange(@RequestParam startDate: String, @RequestParam endDate: String): AbsencesWebModel {
    val absences = absenceRepository.findByEndDateGreaterThanEqualAndStartDateLessThanEqual(LocalDate.parse(startDate), LocalDate.parse(endDate))
    val absencesPojos: List<AbsencePojo> = absences.map{absence ->
      AbsencePojo(absence)
    }
    return AbsencesWebModel(absencesPojos)
  }

  @PreAuthorize( "#oauth2.hasScope('madhatter.entity.read')")
  @GetMapping(value = ["/{date}"], produces = ["application/json"])
  fun getAbsencesForDate(@PathVariable("date") date: String): AbsencesWebModel {
    val dateObj = LocalDate.parse(date)
    val absences: List<Absence> = absenceRepository.findByEndDateGreaterThanEqualAndStartDateLessThanEqual(dateObj, dateObj)
    val absencesPojos: List<AbsencePojo> = absences.map{absence ->
      AbsencePojo(absence)
    }
    return AbsencesWebModel(absencesPojos)
  }

  @PreAuthorize( "#oauth2.hasScope('madhatter.entity.read')")
  @GetMapping(value = ["/maintainer/{maintainerId}"], produces = ["application/json"])
  fun getAbsencesForMaintainer(@PathVariable("maintainerId") maintainerId: Long): AbsencesWebModel {
    val absences = absenceRepository.findByMaintainerId(maintainerId)
    val absencesPojos: List<AbsencePojo> = absences.map{absence ->
      AbsencePojo(absence)
    }
    return AbsencesWebModel(absencesPojos)
  }

  @PreAuthorize( "#oauth2.hasScope('madhatter.athena.entity.create')")
  @PostMapping(value = ["/create"], consumes = [MediaType.APPLICATION_JSON_VALUE])
  fun postAbsence(@RequestBody newAbsence: NewAbsence): ResponseEntity<Any> {
    createAbsenceCommand.execute(newAbsence.startDate, newAbsence.endDate, newAbsence.hours, newAbsence.reason, newAbsence.location, newAbsence.maintainerId)
    return ResponseEntity(HttpStatus.OK)
  }

  @PreAuthorize( "#oauth2.hasScope('madhatter.athena.entity.delete')")
  @DeleteMapping(value = ["/delete/{id}"])
  fun deleteAbsence(@PathVariable id: Long): ResponseEntity<Any> {
    absenceRepository.deleteById(id)
    return ResponseEntity(HttpStatus.OK)
  }
}
