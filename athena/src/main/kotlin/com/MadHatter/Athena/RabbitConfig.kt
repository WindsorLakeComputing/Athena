package com.MadHatter.Athena

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


@Component
@Configuration
class RabbitConfig {
  @Bean
  fun exchange(): TopicExchange {
    return ExchangeBuilder
        .topicExchange(RabbitConstants.EXCHANGE)
        .build() as TopicExchange
  }

  @Bean
  fun athenaConsumeTrainingSchedule(): Queue {
    return QueueBuilder
        .durable(RabbitConstants.QUEUE)
        .build()
  }

  @Bean
  fun athenaConsumeTrainingScheduleBinding(): Binding {
    return BindingBuilder
        .bind(athenaConsumeTrainingSchedule())
        .to(exchange())
        .with(RabbitConstants.ROUTING_KEY_CONSUME)
  }

  @Bean
  fun athenaProduceStuff(): Binding {
    return BindingBuilder.bind(exchange()).to(exchange()).with(RabbitConstants.ROUTING_KEY_PRODUCE)
  }

  @Bean
  fun messageConverter(objectMapper: ObjectMapper): MessageConverter {
    return Jackson2JsonMessageConverter(objectMapper)
  }
}

object RabbitConstants {
  const val EXCHANGE = "minerva"
  const val ROUTING_KEY_CONSUME = "minerva.schedule"
  const val ROUTING_KEY_PRODUCE = "minerva.request.schedule"
  const val QUEUE = "athena.consume.training.schedule"
}
