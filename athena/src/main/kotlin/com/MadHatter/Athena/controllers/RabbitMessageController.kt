package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.minerva.RabbitMessages
import com.MadHatter.Athena.repositories.RabbitMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rabbitMessages")
class RabbitMessageController {

  @Autowired
  lateinit var rabbitMessageRepository: RabbitMessageRepository

  @PreAuthorize( "#oauth2.hasScope('madhatter.athena.entity.admin')")
  @GetMapping(produces = ["application/json"])
  fun getAll(): RabbitMessages = RabbitMessages(rabbitMessageRepository.findAll())
}