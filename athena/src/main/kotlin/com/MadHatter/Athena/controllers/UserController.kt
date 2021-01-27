package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.controllers.webModels.UserName
import com.MadHatter.Athena.commands.UserNameSessionCommandInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController {
  @Autowired
  lateinit var userNameSessionCommand: UserNameSessionCommandInterface

  @PreAuthorize( "#oauth2.hasScope('madhatter.entity.read')")
  @GetMapping(produces = ["application/json"])
  fun getUsername(): UserName = userNameSessionCommand.execute()
}
