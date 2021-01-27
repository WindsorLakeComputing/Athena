package com.MadHatter.Athena.minerva

import com.MadHatter.Athena.RabbitConstants
import com.MadHatter.Athena.repositories.RabbitMessageRepository
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component

@Component
class ScheduleListener {
  @Autowired
  lateinit var rabbitMessageRepo: RabbitMessageRepository

  @RabbitListener(queues = [RabbitConstants.QUEUE])
  fun onMessage(@Headers headers: Map<String, String>, payload: MinervaResultsPojo) {

    val rabbitMessage = RabbitMessage(RabbitConstants.QUEUE, payload.toString())
    rabbitMessageRepo.save(rabbitMessage)
  }
}
