package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.commands.CreateMaintainerCommand
import com.MadHatter.Athena.controllers.webModels.NewMaintainer
import com.MadHatter.Athena.helpers.MaintainerFactory
import com.MadHatter.Athena.models.Certificate
import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.models.MaintainerPojo
import com.MadHatter.Athena.models.Section
import com.MadHatter.Athena.models.Shift
import com.MadHatter.Athena.repositories.CertificateRepository
import com.MadHatter.Athena.repositories.MaintainerRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class MaintainerControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var maintainerRepository: MaintainerRepository

  @MockBean
  lateinit var certificateRepository: CertificateRepository

  @MockBean
  lateinit var createMaintainerCommand: CreateMaintainerCommand

  @Before
  fun setUp() {
    given(maintainerRepository.findById(1)).willReturn(Optional.of(maintainer))
  }

  val mapper = jacksonObjectMapper()

  val maintainer: Maintainer = MaintainerFactory.create(id = 1, certificates = listOf(Certificate("X"), Certificate("L/X")))

  @Test
  fun getAllMaintainers() {
    val maintainer2: Maintainer = MaintainerFactory.create(id = 2)
    given(maintainerRepository.findAll()).willReturn(mutableListOf(maintainer, maintainer2))

    mockMvc.perform(get("/api/maintainers"))
      .andDo(print())
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.maintainers", hasSize<Maintainer>(2)))
      .andExpect(jsonPath("$.maintainers[0].firstName", equalTo("Curtis-'")))
      .andExpect(jsonPath("$.maintainers[0].lastName", equalTo("LeMay")))
      .andExpect(jsonPath("$.maintainers[0].employeeId", equalTo("1234")))
      .andExpect(jsonPath("$.maintainers[0].certificates[0].name", equalTo("X")))
      .andExpect(jsonPath("$.maintainers[0].certificates[1].name", equalTo("L/X")))
      .andExpect(jsonPath("$.maintainers[0].firstShiftPreference.name", equalTo("D")))
      .andExpect(jsonPath("$.maintainers[0].secondShiftPreference.name", equalTo("M")))
      .andExpect(jsonPath("$.maintainers[0].thirdShiftPreference.name", equalTo("S")))
      .andExpect(jsonPath("$.maintainers[0].level", equalTo("3")))
      .andExpect(jsonPath("$.maintainers[0].section.name", equalTo("APG")))
  }

  @Test
  fun getOneMaintainer() {
    mockMvc.perform(get("/api/maintainers/1"))
      .andExpect(status().isOk)
      .andDo(print())
      .andExpect(jsonPath("$.firstName", equalTo("Curtis-'")))
      .andExpect(jsonPath("$.lastName", equalTo("LeMay")))
      .andExpect(jsonPath("$.employeeId", equalTo("1234")))
      .andExpect(jsonPath("$.certificates[0].name", equalTo("X")))
      .andExpect(jsonPath("$.certificates[1].name", equalTo("L/X")))
      .andExpect(jsonPath("$.firstShiftPreference.name", equalTo("D")))
      .andExpect(jsonPath("$.secondShiftPreference.name", equalTo("M")))
      .andExpect(jsonPath("$.thirdShiftPreference.name", equalTo("S")))
      .andExpect(jsonPath("$.level", equalTo("3")))
      .andExpect(jsonPath("$.section.name", equalTo("APG")))
  }

  @Test
  fun updateOneMaintainer() {
    given(maintainerRepository.getOne(1)).willReturn(maintainer)

    val certificates: List<Certificate> = listOf(Certificate("X"))
    given(certificateRepository.getByName("X")).willReturn(certificates)

    val maintainer = MaintainerPojo(MaintainerFactory.create("Henry",
      "Arnold",
      "1456",
      certificates,
      Shift("D", 1),
      Shift("S", 2),
      Shift("M", 3),
      "3",
      "E2",
      Section("APG", 1),
      Shift("S", 2),
      1))

    mockMvc.perform(put("/api/maintainers/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(maintainer))
      .with(SecurityMockMvcRequestPostProcessors.csrf()))
      .andExpect(status().isOk)
  }

  @Test
  fun getNonExistantIdMaintainer() {
    mockMvc.perform(get("/api/maintainers/-1"))
      .andExpect(status().isNotFound)
  }

  @Test
  fun createMaintainerCallsCreateMaintainerCommand() {
    val maintainer = NewMaintainer(
      firstName = "Johnny",
      lastName = "Appleseed"
    )

    given(createMaintainerCommand.execute(maintainer))
      .willReturn(MaintainerFactory.create())

    mockMvc.perform(post("/api/maintainers")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(maintainer))
      .with(SecurityMockMvcRequestPostProcessors.csrf()))
      .andExpect(status().isOk)

    verify(createMaintainerCommand, times(1)).execute(maintainer)
  }

  @Test
  fun createMaintainerFailureReturns500() {
    val maintainer = NewMaintainer(
      firstName = "Johnny",
      lastName = "Appleseed"
    )

    Mockito.doThrow(IllegalStateException()).`when`(createMaintainerCommand).execute(maintainer)

    mockMvc.perform(post("/api/maintainers")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(maintainer))
      .with(SecurityMockMvcRequestPostProcessors.csrf()))
      .andExpect(status().is5xxServerError)
  }
}
