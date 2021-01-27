package com.MadHatter.Athena.repositories

import com.MadHatter.Athena.minerva.RabbitMessage
import org.springframework.data.jpa.repository.JpaRepository

interface RabbitMessageRepository: JpaRepository<RabbitMessage, Long>