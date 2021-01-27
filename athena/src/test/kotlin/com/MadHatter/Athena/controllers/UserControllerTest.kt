package com.MadHatter.Athena.controllers

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Test
  fun `Get User Returns GivenName Of Logged In User`() {
    val givenName = "Hermes Weasley"

    mockMvc.perform(get("/api/user")
      .secure(true))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.given_name", equalTo(givenName)))
  }

}
