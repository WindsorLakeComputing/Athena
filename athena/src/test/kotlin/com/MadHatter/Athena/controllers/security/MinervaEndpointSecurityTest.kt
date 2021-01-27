package com.MadHatter.Athena.controllers.security

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("security_test")
class MinervaEndpointSecurityTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var webApplicationContext: WebApplicationContext

  @get:Rule
  val wm = WireMockRule(5001)

  @Before
  fun setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
      .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
      .build()

    wm.start()

    stubFor(WireMock.post(urlEqualTo("/athena-generate-request"))
      .willReturn(aResponse()))
  }

  @After
  internal fun afterEach() {
    wm.stop()
  }

  @Test
  @DisplayName("Deleter cannot post to minerva uri")
  @WithMockOAuth2Scope(scopes = ["madhatter.athena.entity.delete"])
  fun deleterCantPostToMinervaUri() {
    val requestBuilder: MockHttpServletRequestBuilder =
      post("/api/minerva")
        .secure(true)
        .with(csrf())

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @DisplayName("Admin can post to minerva uri")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read", "madhatter.athena.entity.admin"])
  fun adminCanPostToMinervaUri() {
    val requestBuilder: MockHttpServletRequestBuilder =
      post("/api/minerva")
        .secure(true)
        .with(csrf())

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk)
  }
}
