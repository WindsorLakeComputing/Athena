package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.Absence
import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.models.Section
import com.MadHatter.Athena.models.Shift
import com.MadHatter.Athena.repositories.AbsenceRepository
import com.MadHatter.Athena.repositories.MaintainerRepository
import com.MadHatter.Athena.commands.CreateAbsenceCommand
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.util.*

@RunWith(SpringRunner::class)
@AutoConfigureStubRunner
class CreateAbsenceCommandTest {
  @Mock
  lateinit var maintainerRepository: MaintainerRepository

  @Mock
  lateinit var absenceRepository: AbsenceRepository

  @InjectMocks
  private var createAbsenceCommand = CreateAbsenceCommand()

  private val fakeMaintainer: Maintainer = Maintainer("John",
      "Loh",
      "1980",
      listOf(),
      Shift("D"),
      Shift("S"),
      Shift("M"),
      "7",
      "string2",
      Section("APG"),
      Shift("D"),
      mutableListOf(),
      mutableListOf(),
      1)


  @Test
  fun createAbsenceSavesANewAbsence() {
    val expectedAbsence = Absence(
      LocalDate.of(2019, 5, 13),
      LocalDate.of(2019, 5, 17),
      fakeMaintainer,
      "Broken Back",
      "Reno",
      "2100-2400")

    given(maintainerRepository.findById(1)).willReturn(Optional.of(fakeMaintainer))

    createAbsenceCommand.execute("2019-05-13", "2019-05-17", "2100-2400", "Broken Back", "Reno", 1)

    val absenceRepoCaptor = ArgumentCaptor.forClass(Absence::class.java)

    verify(absenceRepository, times(1)).save(absenceRepoCaptor.capture())
    val capturedAbsence = absenceRepoCaptor.allValues[0]
    assertEquals(expectedAbsence, capturedAbsence)
  }
}
