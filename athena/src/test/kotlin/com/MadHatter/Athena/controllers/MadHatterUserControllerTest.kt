package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.commands.CreateUserCommand
import com.MadHatter.Athena.commands.GetAllUsersCommand
import com.MadHatter.Athena.models.MadHatterUser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.Matchers.any
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.lang.IllegalStateException

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class MadHatterUserControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  val mapper = jacksonObjectMapper()

  @MockBean
  lateinit var createUserCommand: CreateUserCommand

  @MockBean
  lateinit var getAllUsersCommand: GetAllUsersCommand

  @Test
  fun `Get Mad Hatter Users`() {
    val user = MadHatterUser(firstName = "First",
      lastName = "Last",
      email = "first@last.com",
      amu = "badAmu")
    BDDMockito.given(getAllUsersCommand.execute()).willReturn(listOf(user))

    mockMvc.perform(MockMvcRequestBuilders.get("/api/MadHatterUsers")
      .secure(true))
      .andExpect(MockMvcResultMatchers.status().isOk)
      .andExpect(MockMvcResultMatchers.jsonPath("$.madHatterUsers", hasSize<MadHatterUser>(1)))
      .andExpect(MockMvcResultMatchers.jsonPath("$.madHatterUsers[0].lastName", equalTo(user.lastName)))
      .andExpect(MockMvcResultMatchers.jsonPath("$.madHatterUsers[0].firstName", equalTo(user.firstName)))
      .andExpect(MockMvcResultMatchers.jsonPath("$.madHatterUsers[0].email", equalTo(user.email)))

    Mockito.verify(getAllUsersCommand, Mockito.times(1)).execute()
  }

  @Test
  fun `Create User returns 200 and passes User to the createUserCommand`() {
    val user = MadHatterUser(
      firstName = "First",
      lastName = "Last",
      email = "first@last.com",
      amu = "badAmu")

    given(createUserCommand.execute(user)).willReturn(user)

    mockMvc.perform(MockMvcRequestBuilders.post("/api/MadHatterUsers")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(user))
      .with(SecurityMockMvcRequestPostProcessors.csrf()))
      .andExpect(MockMvcResultMatchers.status().isOk)

    Mockito.verify(createUserCommand, Mockito.times(1)).execute(user)
  }

  @Test
  fun `Try to Create User and Fails`() {
    val user = MadHatterUser(firstName = "perfectlyFineUserName",
      lastName = "Last",
      email = "first@last.com",
      amu = "badAmu")

    given(createUserCommand.execute(user))
      .willThrow(IllegalStateException())

    mockMvc.perform(MockMvcRequestBuilders.post("/api/MadHatterUsers")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(user))
      .with(SecurityMockMvcRequestPostProcessors.csrf()))
      .andExpect(MockMvcResultMatchers.status().is5xxServerError)
  }
}
