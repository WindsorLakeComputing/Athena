package com.MadHatter.Athena.controllers.security

import com.MadHatter.Athena.controllers.webModels.NewAbsence
import com.MadHatter.Athena.repositories.AbsenceRepository
import com.MadHatter.Athena.commands.CreateAbsenceCommand
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
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
class AbsenceEndpointSecurityTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var webApplicationContext: WebApplicationContext

  @MockBean
  lateinit var createAbsenceCommand: CreateAbsenceCommand

  @MockBean
  lateinit var absenceRepository: AbsenceRepository


  val mapper = jacksonObjectMapper()

  val absence = NewAbsence(3,
    "2019-05-13",
    "2019-05-17",
    "Broken Back",
    "Reno",
    "2100-2400")

  @Before
  fun setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
      .apply<DefaultMockMvcBuilder>(springSecurity())
      .build()
  }

  private fun getPostRequest(urlTemplate: String, contentType: MediaType, content: String):
    MockHttpServletRequestBuilder {
    return post(urlTemplate)
      .secure(true)
      .contentType(contentType)
      .content(content)
      .with(csrf())
  }

  @Test
  @DisplayName("Hoot cannot get absences for range")
  @WithMockOAuth2Scope(scopes = ["hoot.a.nanny"])
  fun hooterCantGetAbsencesForRange() {
    val requestBuilder = get("/api/absences")
      .param("startDate", "2019-05-01")
      .param("endDate", "2019-05-10")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(status().isForbidden)
  }

  @Test
  @DisplayName("Reader can get absences for range")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read"])
  fun readerCanGetAbsencesForRange() {
    val requestBuilder = get("/api/absences")
      .param("startDate", "2019-05-01")
      .param("endDate", "2019-05-10")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(status().isOk)
  }

  @Test
  @DisplayName("Hoot cannot get absences for date")
  @WithMockOAuth2Scope(scopes = ["hoot.a.nanny"])
  fun hooterCantGetAbsencesForDate() {
    val requestBuilder = get("/api/absences/2019-05-01")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(status().isForbidden)
  }

  @Test
  @DisplayName("Reader can get absences for date")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read"])
  fun readerCanGetAbsencesForDate() {
    val requestBuilder = get("/api/absences/2019-05-01")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(status().isOk)
  }

  @Test
  @DisplayName("Hoot cannot get absences for maintainer")
  @WithMockOAuth2Scope(scopes = ["hoot.a.nanny"])
  fun hooterCantGetAbsencesForMaintainer() {
    val requestBuilder = get("/api/absences/maintainer/1")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(status().isForbidden)
  }

  @Test
  @DisplayName("Reader can get absences for maintainer")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read"])
  fun readerCanGetAbsencesForMaintainer() {
    val requestBuilder = get("/api/absences/maintainer/1")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(status().isOk)
  }

  @Test
  @DisplayName("Reader cannot post to create Absence")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read"])
  fun readerCantPostToCreateAbsence() {
    val requestBuilder: MockHttpServletRequestBuilder =
      getPostRequest("/api/absences/create", MediaType.APPLICATION_JSON,
        mapper.writeValueAsString(absence))

    mockMvc.perform(requestBuilder).andExpect(status().isForbidden)
  }

  @Test
  @DisplayName("Creator can post to create Absence")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read", "madhatter.athena.entity.create"])
  fun creatorCanPostToCreateAbsence() {
    val requestBuilder: MockHttpServletRequestBuilder =
      getPostRequest("/api/absences/create", MediaType.APPLICATION_JSON,
        mapper.writeValueAsString(absence))

    mockMvc.perform(requestBuilder).andExpect(status().isOk)
  }

  @Test
  @DisplayName("Edit cannot delete absence")
  @WithMockOAuth2Scope(scopes = ["madhatter.athena.entity.edit"])
  fun editCantDeleteAbsence() {
    val requestBuilder = delete("/api/absences/delete/4")
      .secure(true)
      .with(csrf())

    mockMvc.perform(requestBuilder).andExpect(status().isForbidden)
  }

  @Test
  @DisplayName("Delete can delete absence")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read", "madhatter.athena.entity.delete"])
  fun deleteCanDeleteAbsence() {
    val requestBuilder = delete("/api/absences/delete/5")
      .secure(true)
      .with(csrf())

    mockMvc.perform(requestBuilder).andExpect(status().isOk)
  }
}
