package com.MadHatter.Athena.minerva

import com.MadHatter.Athena.repositories.RabbitMessageRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@AutoConfigureStubRunner
class ScheduleListenerTest {

  @Mock
  lateinit var rabbitMessageRepo: RabbitMessageRepository

  @InjectMocks
  private var scheduleListener = ScheduleListener()

  @Test
  fun onMessageUpdatesApiRequest() {
    val expectedMinervaResultsPojo = MinervaResultsPojo(1,2,3,4,5,6,7)
    val expectedRabbitMessage = RabbitMessage(
      "athena.consume.training.schedule",
      expectedMinervaResultsPojo.toString())

    scheduleListener.onMessage(mapOf(), expectedMinervaResultsPojo)

    verify(rabbitMessageRepo, times(1)).save(expectedRabbitMessage)
  }
}