package com.MadHatter.Athena.controllers.security

import com.MadHatter.Athena.helpers.AuthenticationTestHelpers
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
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
class UserEndpointSecurityTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var webApplicationContext: WebApplicationContext

  val givenName = "Pigwidgeon Weasley"

  @Before
  fun setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
      .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
      .build()
  }

  @Test
  @DisplayName("Hoot cannot get username")
  fun hooterCantGetUsername() {
    val requestBuilder = get("/api/user")
      .with(SecurityMockMvcRequestPostProcessors.authentication(
        AuthenticationTestHelpers.getMockAuthentication(givenName, "hoot.a.nanny")))
      .sessionAttr("scopedTarget.oauth2ClientContext",
        AuthenticationTestHelpers.getMockOauth2ClientContext())
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @DisplayName("Reader can get username")
  fun readerCanGetUsername() {
    val requestBuilder = get("/api/user")
      .with(SecurityMockMvcRequestPostProcessors.authentication(
        AuthenticationTestHelpers.getMockAuthentication(givenName, "madhatter.entity.read")))
      .sessionAttr("scopedTarget.oauth2ClientContext",
        AuthenticationTestHelpers.getMockOauth2ClientContext())
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk)
  }
}
