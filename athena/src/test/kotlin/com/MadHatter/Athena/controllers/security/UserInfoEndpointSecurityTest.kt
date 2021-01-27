package com.MadHatter.Athena.controllers.security

import com.MadHatter.Athena.commands.GetUserInfoCommand
import com.MadHatter.Athena.helpers.UserInfoFactory
import com.MadHatter.Athena.models.UserInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("security_test")
class UserInfoEndpointSecurityTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var getUserInfoCommand: GetUserInfoCommand // Will return null when called in controller

  @Test
  @DisplayName("Returns user info when request made with Authorization token")
  fun userInfoEndpointIsAvailableWithoutOAuth() {
    val requestBuilder: MockHttpServletRequestBuilder = get("/userinfo")
      .header("Authorization", "We bare bearer tokens")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk)
  }
}
