package com.MadHatter.Athena.repositories

import com.MadHatter.Athena.minerva.IntegrationRequest
import org.springframework.data.jpa.repository.JpaRepository

interface IntegrationRequestRepository: JpaRepository<IntegrationRequest, Long> {
}