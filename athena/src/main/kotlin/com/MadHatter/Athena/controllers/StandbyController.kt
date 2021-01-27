package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.controllers.webModels.NewStandby
import com.MadHatter.Athena.repositories.StandbyRepository
import com.MadHatter.Athena.commands.CreateStandbyCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/maintainer/standby")
class StandbyController {

  @Autowired
  lateinit var standbyRepository: StandbyRepository

  @Autowired
  lateinit var createStandbyCommand: CreateStandbyCommand

  @PreAuthorize( "#oauth2.hasScope('madhatter.athena.entity.create')")
  @PostMapping(value = [""], consumes = [MediaType.APPLICATION_JSON_VALUE])
  fun postStandby(@RequestBody newStandby: NewStandby): ResponseEntity<Any> {
    createStandbyCommand.execute(newStandby.startDate, newStandby.endDate, newStandby.maintainerId)
    return ResponseEntity(HttpStatus.OK)
  }

  @PreAuthorize( "#oauth2.hasScope('madhatter.athena.entity.delete')")
  @DeleteMapping(value = ["/{id}"])
  fun deleteStandby(@PathVariable id: Long): ResponseEntity<Any> {
    standbyRepository.deleteById(id)
    return ResponseEntity(HttpStatus.OK)
  }
}
