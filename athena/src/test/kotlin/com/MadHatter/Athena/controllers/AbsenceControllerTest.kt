package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.controllers.webModels.NewAbsence
import com.MadHatter.Athena.helpers.MaintainerFactory
import com.MadHatter.Athena.models.Absence
import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.repositories.AbsenceRepository
import com.MadHatter.Athena.commands.CreateAbsenceCommand
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasSize
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AbsenceControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var absenceRepository: AbsenceRepository

  @MockBean
  lateinit var createAbsenceCommand: CreateAbsenceCommand

  val mapper = jacksonObjectMapper()

  @Test
  fun getAbsencesForDateWhenTheyExist() {

    val maintainer: Maintainer = MaintainerFactory.create(id = 1)

    val date = LocalDate.parse("2019-05-14")
    val absence = Absence(date,
      date,
      maintainer,
      "Rodeo",
      "El Paso",
      "2100-2400",
      1)

    given(absenceRepository.findByEndDateGreaterThanEqualAndStartDateLessThanEqual(date, date)).willReturn(listOf(absence))

    mockMvc.perform(get("/api/absences/2019-05-14"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.absences[0].id", equalTo(1)))
      .andExpect(jsonPath("$.absences[0].startDate", equalTo("2019-05-14")))
      .andExpect(jsonPath("$.absences[0].endDate", equalTo("2019-05-14")))
      .andExpect(jsonPath("$.absences[0].reason", equalTo("Rodeo")))
      .andExpect(jsonPath("$.absences[0].location", equalTo("El Paso")))
  }

  @Test
  fun getAbsencesForRangeWhenTheyExist() {

    val maintainer: Maintainer = MaintainerFactory.create(id = 1)

    val startDate = LocalDate.parse("2019-05-14")
    val endDate = LocalDate.parse("2019-05-24")

    val absence = Absence(startDate,
      startDate,
      maintainer,
      "Rodeo",
      "El Paso",
      "2100-2400",
      1)

    val absence2 = Absence(endDate,
      endDate,
      maintainer,
      "Shot",
      "London",
      "1100-2400",
      2)

    given(absenceRepository.findByEndDateGreaterThanEqualAndStartDateLessThanEqual(startDate, endDate)).willReturn(listOf(absence, absence2))

    mockMvc.perform(get("/api/absences?startDate=2019-05-14&endDate=2019-05-24")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.absences", hasSize<Absence>(2)))
      .andExpect(jsonPath("$.absences[0].id", equalTo(1)))
      .andExpect(jsonPath("$.absences[0].startDate", equalTo("2019-05-14")))
      .andExpect(jsonPath("$.absences[0].endDate", equalTo("2019-05-14")))
      .andExpect(jsonPath("$.absences[0].reason", equalTo("Rodeo")))
      .andExpect(jsonPath("$.absences[0].location", equalTo("El Paso")))
      .andExpect(jsonPath("$.absences[1].id", equalTo(2)))
      .andExpect(jsonPath("$.absences[1].startDate", equalTo("2019-05-24")))
      .andExpect(jsonPath("$.absences[1].endDate", equalTo("2019-05-24")))
      .andExpect(jsonPath("$.absences[1].reason", equalTo("Shot")))
      .andExpect(jsonPath("$.absences[1].location", equalTo("London")))
  }

  @Test
  fun respondsOkWhenNoAbsencesExistForDate() {

    val date = LocalDate.parse("2019-05-14")

    given(absenceRepository.findByEndDateGreaterThanEqualAndStartDateLessThanEqual(date, date)).willReturn(listOf())

    mockMvc.perform(get("/api/absences/2019-05-14"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.absences", Matchers.empty<Absence>()))
  }

  @Test
  fun getAbsencesForMaintainerWhenTheyExist() {
    val maintainer: Maintainer = MaintainerFactory.create(id = 1)

    val date = LocalDate.parse("2019-05-14")
    val absence = Absence(date,
      date,
      maintainer,
      "Rodeo",
      "El Paso",
      "2100-2400",
      1)

    given(absenceRepository.findByMaintainerId(1)).willReturn(listOf(absence))

    mockMvc.perform(get("/api/absences/maintainer/1"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.absences[0].id", equalTo(1)))
      .andExpect(jsonPath("$.absences[0].startDate", equalTo("2019-05-14")))
      .andExpect(jsonPath("$.absences[0].endDate", equalTo("2019-05-14")))
      .andExpect(jsonPath("$.absences[0].reason", equalTo("Rodeo")))
      .andExpect(jsonPath("$.absences[0].location", equalTo("El Paso")))
  }

  @Test
  fun respondsOkWhenNoAbsencesExistForMaintainer() {

    given(absenceRepository.findByMaintainerId(2)).willReturn(listOf())

    mockMvc.perform(get("/api/absences/maintainer/2"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.absences", Matchers.empty<Absence>()))
  }

  @Test
  fun afterAddingNewAbsenceCanFindNewAbsence() {
    val absence = NewAbsence(2,
      "2019-05-13",
      "2019-05-17",
      "Broken Back",
      "Reno",
      "2100-2400")

    mockMvc.perform(post("/api/absences/create")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(absence))
      .with(csrf()))
      .andExpect(status().isOk)

    verify(createAbsenceCommand, times(1)).execute("2019-05-13", "2019-05-17", "2100-2400", "Broken Back", "Reno",2)
  }

  @Test
  fun respondsOkWhenExistingAbsenceDelete() {
    mockMvc.perform(delete("/api/absences/delete/4"))
      .andExpect(status().isOk)

    val absenceRepoCaptor = ArgumentCaptor.forClass(Long::class.java)
    verify(absenceRepository, times(1)).deleteById(absenceRepoCaptor.capture())
  }
}
