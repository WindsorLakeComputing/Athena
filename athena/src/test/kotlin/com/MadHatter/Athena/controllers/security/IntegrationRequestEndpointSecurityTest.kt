package com.MadHatter.Athena.controllers.security

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("security_test")
class IntegrationRequestEndpointSecurityTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var webApplicationContext: WebApplicationContext

  @Before
  fun setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
      .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
      .build()
  }

  @Test
  @DisplayName("Deleter cannot see integration requests")
  @WithMockOAuth2Scope(scopes = ["madhatter.athena.entity.delete"])
  fun deleterCantGetIntegrationRequests() {
    val requestBuilder = get("/api/integrationRequests")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @DisplayName("Admin can see integration requests")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read", "madhatter.athena.entity.admin"])
  fun adminCanGetIntegrationRequests() {
    val requestBuilder = get("/api/integrationRequests")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk)
  }
}
