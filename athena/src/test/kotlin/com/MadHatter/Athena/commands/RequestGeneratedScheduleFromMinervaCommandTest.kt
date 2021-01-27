package com.MadHatter.Athena.commands

import com.MadHatter.Athena.commands.RequestGeneratedScheduleFromMinervaCommand
import com.MadHatter.Athena.minerva.IntegrationRequest
import com.MadHatter.Athena.repositories.IntegrationRequestRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class RequestGeneratedScheduleFromMinervaCommandTest {
  @Mock
  lateinit var rabbitTemplate: RabbitTemplate

  @Mock
  lateinit var integrationRequestRepository: IntegrationRequestRepository

  @InjectMocks
  val requestGeneratedSchedule = RequestGeneratedScheduleFromMinervaCommand()

  @Captor
  lateinit var captor: ArgumentCaptor<IntegrationRequest>

  @Test
  fun `Send a message to the request schedule queue`() {
    requestGeneratedSchedule.execute()

    verify(rabbitTemplate).convertAndSend(
      eq("minerva"),
      eq("minerva.request.schedule"),
      eq("generate schedule request")
    )
  }

  @Test
  fun `Creates a record of when Minerva was requested to run`() {
    requestGeneratedSchedule.execute()

    verify(integrationRequestRepository, times(1))
      .save(captor.capture())
  }

  @Test
  fun `Creates a record of when Minerva fails to be requested to run`() {
    given(rabbitTemplate.convertAndSend(anyString(), anyString())).willThrow(IllegalArgumentException())

    requestGeneratedSchedule.execute()

    verify(integrationRequestRepository, times(1))
      .save(captor.capture())

  }
}
