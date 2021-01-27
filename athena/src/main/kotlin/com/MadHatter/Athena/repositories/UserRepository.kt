package com.MadHatter.Athena.repositories

import com.MadHatter.Athena.models.MadHatterUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<MadHatterUser, Long> {
  fun getByEmail(email: String): MadHatterUser?
}
