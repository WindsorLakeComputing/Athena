package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.MadHatterUser
import com.MadHatter.Athena.repositories.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class GetAllUsersCommandTest {
  @Mock
  lateinit var userRepository: UserRepository

  @InjectMocks
  private var getAllUsersCommand = GetAllUsersCommand()

  @Test
  fun getAllUsers() {
    val user = MadHatterUser(
      firstName = "Johnny",
      lastName = "Appleseed",
      email = "Johnny.Appleseed@usersc.com",
      amu = "badAmu")

    given(userRepository.findAll()).willReturn(listOf(user))

    val allUsers = getAllUsersCommand.execute()

    verify(userRepository, times(1)).findAll()
    assert(allUsers.size == 1)
    assert(allUsers[0] == user)
  }
}
