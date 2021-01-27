package com.MadHatter.Athena.repositories

import com.MadHatter.Athena.models.Standby
import org.springframework.data.jpa.repository.JpaRepository

interface StandbyRepository : JpaRepository<Standby, Long>
