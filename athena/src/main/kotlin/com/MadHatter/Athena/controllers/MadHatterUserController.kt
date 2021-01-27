package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.commands.CreateUserCommand
import com.MadHatter.Athena.commands.GetAllUsersCommand
import com.MadHatter.Athena.controllers.webModels.ErrorResponse
import com.MadHatter.Athena.controllers.webModels.MadHatterUsersWebModel
import com.MadHatter.Athena.models.MadHatterUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/MadHatterUsers")
class MadHatterUserController {
  @Autowired
  lateinit var createUserCommand: CreateUserCommand

  @Autowired
  lateinit var getAllUsersCommand: GetAllUsersCommand

  @GetMapping
  fun getAllMadHatterUsers(): ResponseEntity<Any> = try {
     ResponseEntity.ok(MadHatterUsersWebModel(getAllUsersCommand.execute()))
  } catch (ex: Exception) {
    ResponseEntity(ErrorResponse("Failed to fetch users"), HttpStatus.INTERNAL_SERVER_ERROR)
  }

  @PreAuthorize( "#oauth2.hasScope('madhatter.athena.entity.create')")
  @PostMapping(produces = ["application/json"])
  fun createUser(@RequestBody user: MadHatterUser): ResponseEntity<Any> = try {
    createUserCommand.execute(user)
    ResponseEntity(HttpStatus.OK)
  } catch (ex: Exception) {
    ResponseEntity(ErrorResponse("Failed to Create MadHatterUser."), HttpStatus.INTERNAL_SERVER_ERROR)
  }
}
