package com.MadHatter.Athena.repositories

import com.MadHatter.Athena.models.Certificate
import org.springframework.data.jpa.repository.JpaRepository

interface CertificateRepository : JpaRepository<Certificate, Long> {
  fun getByName(name: String): List<Certificate>
}