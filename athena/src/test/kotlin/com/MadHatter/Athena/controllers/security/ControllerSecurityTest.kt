package com.MadHatter.Athena.controllers.security

import com.MadHatter.Athena.controllers.webModels.NewAbsence
import com.MadHatter.Athena.helpers.AuthenticationTestHelpers
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern


@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("security_test")
class ControllerSecurityTest {
  @Autowired
  lateinit var mockMvc: MockMvc

  val mapper = jacksonObjectMapper()

  @Test
  fun testUnauthenticated() {
    mockMvc.perform(MockMvcRequestBuilders.get("/").secure(true))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
  }

  @Test
  @WithMockUser("test-user")
  fun testAuthenticatedButNotAuthorized() {
    mockMvc.perform(MockMvcRequestBuilders.get("/").secure(true))
        .andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @WithMockUser("test-user")
  fun nonHttpsRequestShouldRedirectToHttps() {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/maintainers").secure(false))
      .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
  }

  @Test
  @WithMockUser("test-user")
  fun nonCsrfPostRequestShouldFail() {

    val absence = NewAbsence(2,
      "2019-05-13",
      "2019-05-17",
      "Broken Back",
      "Reno",
      "2100-2400")

    mockMvc.perform(MockMvcRequestBuilders.post("/api/absences/create").secure(true)
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(absence)))
      .andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @WithMockUser("test-user")
  fun responseContainsCSPHeaders() {

    val requestBuilder = MockMvcRequestBuilders.get("/")
      .with(SecurityMockMvcRequestPostProcessors.authentication(
        AuthenticationTestHelpers.getMockAuthentication("Pigwidgeon Weasley", "madhatter.entity.read")))
      .sessionAttr("scopedTarget.oauth2ClientContext",
        AuthenticationTestHelpers.getMockOauth2ClientContext())
      .secure(true)

    val result = mockMvc.perform(requestBuilder)
      result.andExpect(MockMvcResultMatchers.header().exists("Content-Security-Policy"))
  }

  @Test
  @WithMockUser("test-user")
  fun `Redirects all requests to HTTPS`() {
    
    mockMvc.perform(MockMvcRequestBuilders.get("/").secure(false))
      .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
      .andExpect(redirectedUrlPattern("https://**"))
  }
}
