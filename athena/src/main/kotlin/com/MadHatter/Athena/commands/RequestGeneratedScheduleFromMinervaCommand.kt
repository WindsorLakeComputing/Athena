package com.MadHatter.Athena.commands

import com.MadHatter.Athena.RabbitConstants
import com.MadHatter.Athena.minerva.IntegrationRequest
import com.MadHatter.Athena.repositories.IntegrationRequestRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp


@Service
class RequestGeneratedScheduleFromMinervaCommand {
  @Autowired
  lateinit var integrationRequestRepository: IntegrationRequestRepository

  @Autowired
  lateinit var rabbitTemplate: RabbitTemplate

  fun execute() =
    try {
      rabbitTemplate.convertAndSend(
        RabbitConstants.EXCHANGE,
        RabbitConstants.ROUTING_KEY_PRODUCE,
        "generate schedule request")
        .also { saveIntegrationRequest("pushed request to minerva to generate schedule") }
    } catch (e: Exception) {
      saveIntegrationRequest(e.message)
    }

  private fun saveIntegrationRequest(message: String?) =
    integrationRequestRepository.save(IntegrationRequest(
      Timestamp(System.currentTimeMillis()),
      message
    ))
}
