package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.minerva.IntegrationRequests
import com.MadHatter.Athena.repositories.IntegrationRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/integrationRequests")
class IntegrationRequestController {

  @Autowired
  lateinit var integrationRequestRepository: IntegrationRequestRepository

  @PreAuthorize( "#oauth2.hasScope('madhatter.athena.entity.admin')")
  @GetMapping(produces = ["application/json"])
  fun getAll(): IntegrationRequests = IntegrationRequests(integrationRequestRepository.findAll())
}