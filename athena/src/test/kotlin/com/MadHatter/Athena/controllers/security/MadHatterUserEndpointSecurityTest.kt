package com.MadHatter.Athena.controllers.security

import com.MadHatter.Athena.models.MadHatterUser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("security_test")
class MadHatterUserEndpointSecurityTest {
  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var webApplicationContext: WebApplicationContext

  val mapper = jacksonObjectMapper()

  @Before
  fun setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
      .apply<DefaultMockMvcBuilder>(springSecurity())
      .build()
  }

  @Test
  @WithMockOAuth2Scope(scopes = ["hoot.a.nanny"])
  fun `Hooter Cant Get the Mad Hatter Users`() {
    val requestBuilder = get("/api/MadHatterUsers")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(status().isForbidden)
  }

  @Test
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read"])
  fun `Reader Can Get the Mad Hatter Users`() {
    val requestBuilder = get("/api/MadHatterUsers")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(status().isOk)
  }

  @Test
  @WithMockOAuth2Scope(scopes = ["hoot.a.nanny"])
  fun `Hooter Cant Create A User`() {
    val requestBuilder = post("/api/MadHatterUsers")
      .secure(true)
      .contentType(MediaType.APPLICATION_JSON)
      .content("")
      .with(csrf())

    mockMvc.perform(requestBuilder).andExpect(status().isForbidden)
  }

  @Test
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read", "madhatter.athena.entity.create"])
  fun `Creator Can Create A User`() {
    val user = MadHatterUser(
      firstName = "First",
      lastName = "Last",
      email = "first@last.com",
      amu = "badAmu")

    val requestBuilder = post("/api/MadHatterUsers")
      .secure(true)
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(user))
      .with(csrf())

    mockMvc.perform(requestBuilder).andExpect(status().isOk)
  }
}
