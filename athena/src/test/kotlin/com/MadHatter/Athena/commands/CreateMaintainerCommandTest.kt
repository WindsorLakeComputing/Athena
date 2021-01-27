package com.MadHatter.Athena.commands

import com.MadHatter.Athena.controllers.webModels.NewMaintainer
import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.models.Section
import com.MadHatter.Athena.repositories.MaintainerRepository
import com.MadHatter.Athena.repositories.SectionRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class CreateMaintainerCommandTest {
  @Mock
  lateinit var maintainerRepository: MaintainerRepository

  @Mock
  lateinit var sectionRepository: SectionRepository

  @InjectMocks
  private var createMaintainerCommand = CreateMaintainerCommand()

  @Test
  fun createsMaintainer() {
    val expectedMaintainer = Maintainer(
      firstName = "Johnny",
      lastName = "Appleseed",
      employeeId = "0",
      level = "1",
      rank = "0",
      section = Section("APG", 1))

    given(sectionRepository.getByName(anyString())).willReturn(listOf(Section("APG", 1)))
    given(maintainerRepository.save(expectedMaintainer)).willReturn(expectedMaintainer)

    createMaintainerCommand.execute(NewMaintainer("Johnny", "Appleseed"))

    verify(maintainerRepository, times(1)).save(expectedMaintainer)
  }
}