package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.commands.GetUserInfoCommand
import com.MadHatter.Athena.models.AthenaUserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/userinfo")
class UserInfoController {

  @Autowired
  lateinit var getUserInfoCommand: GetUserInfoCommand

  @GetMapping
  fun getUserinfo(@RequestHeader("Authorization") authorizationToken: String?): ResponseEntity<AthenaUserInfo> {
    return getUserInfoCommand.execute(authorizationToken)
  }
}
