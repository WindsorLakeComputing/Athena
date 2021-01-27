package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.commands.RequestGeneratedScheduleFromMinervaCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/minerva")
class MinervaController {

  @Autowired
  lateinit var requestGeneratedScheduleFromMinervaCommand: RequestGeneratedScheduleFromMinervaCommand

  @PreAuthorize( "#oauth2.hasScope('madhatter.athena.entity.admin')")
  @PostMapping
  fun postToMinerva() = requestGeneratedScheduleFromMinervaCommand.execute()
}
