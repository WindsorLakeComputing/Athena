package com.MadHatter.Athena.commands

import com.MadHatter.Athena.commands.GetAllMaintainersCommand
import com.MadHatter.Athena.helpers.MaintainerFactory
import com.MadHatter.Athena.models.Shift
import com.MadHatter.Athena.repositories.MaintainerRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@AutoConfigureStubRunner
class GetAllMaintainersCommandTest {
  @Mock
  lateinit var maintainerRepository: MaintainerRepository

  @InjectMocks
  lateinit var getAllMaintainersCommand: GetAllMaintainersCommand

  @Test
  fun `It sorts the list of maintainers by Shift`(){
    val dummy1 = MaintainerFactory.create(shift = Shift("M"))
    val dummy2 = MaintainerFactory.create(shift = Shift("D"))
    val dummy3 = MaintainerFactory.create(shift = Shift("S"))
    val list = listOf(dummy1, dummy2, dummy3)
    given(maintainerRepository.findAll()).willReturn(list)

    val sorted = getAllMaintainersCommand.execute()

    assert(3 == sorted.size)

    assert("D" == sorted[0].shift?.name)
    assert("S" == sorted[1].shift?.name)
    assert("M" == sorted[2].shift?.name)
  }

  @Test
  fun `It sorts the list of maintainers by Shift then Level`(){
    val dummy1 = MaintainerFactory.create(shift = Shift("M"), level = "1")
    val dummy2 = MaintainerFactory.create(shift = Shift("M"), level = "7")
    val dummy3 = MaintainerFactory.create(shift = Shift("S"), level = "3")
    val list = listOf(dummy1, dummy2, dummy3)
    given(maintainerRepository.findAll()).willReturn(list)

    val sortedResponse = getAllMaintainersCommand.execute()

    assert(3 == sortedResponse.size)
    assert("3" == sortedResponse[0].level)
    assert("7" == sortedResponse[1].level)
    assert("1" == sortedResponse[2].level)
  }

  @Test
  fun `It sorts the list of maintainers by Shift then Level then Last Name`(){
    val dummy1 = MaintainerFactory.create(shift = Shift("M"), level = "3", lastName = "Caaaa")
    val dummy2 = MaintainerFactory.create(shift = Shift("M"), level = "3", lastName = "Baaaa")
    val dummy3 = MaintainerFactory.create(shift = Shift("M"), level = "3", lastName = "Aaaaa")
    val list = listOf(dummy1, dummy2, dummy3)
    given(maintainerRepository.findAll()).willReturn(list)

    val sorted = getAllMaintainersCommand.execute()

    assert(3 == sorted.size)
    assert("Aaaaa" == sorted[0].lastName)
    assert("Baaaa" == sorted[1].lastName)
    assert("Caaaa" == sorted[2].lastName)
  }
}
