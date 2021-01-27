package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.helpers.MaintainerFactory
import com.MadHatter.Athena.models.Certificate
import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.repositories.MaintainerRepository
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional


@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class MinervaControllerTest {

  @MockBean
  lateinit var rabbitTemplate: RabbitTemplate

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var maintainerRepository: MaintainerRepository

  @Test
  @Transactional
  fun sendMinervaRequest() {
    mockMvc.perform(post("/api/minerva")
      .with(csrf()))
      .andExpect(status().isOk)

    Mockito.verify(rabbitTemplate).convertAndSend(
      ArgumentMatchers.eq("minerva"),
      ArgumentMatchers.eq("minerva.request.schedule"),
      ArgumentMatchers.eq("generate schedule request")
    )
  }
}
