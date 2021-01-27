package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.MadHatterUser
import com.MadHatter.Athena.repositories.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class CreateUserCommandTest {
  @Mock
  lateinit var userRepository: UserRepository

  @InjectMocks
  private var createUserCommand = CreateUserCommand()

  @Test
  fun `Creates a user`() {

    val expectedUser = MadHatterUser(
      firstName = "Johnny",
      lastName = "Appleseed",
      email = "Johnny.Appleseed@usersc.com",
      amu = "badAmu")

    given(userRepository.getByEmail(anyString())).willReturn(null)
    given(userRepository.save(expectedUser)).willReturn(expectedUser)

    createUserCommand.execute(expectedUser)

    verify(userRepository, times(1)).save(expectedUser)
  }

  @Test(expected = Exception::class)
  fun `Does not allow duplicate user to be created`() {
    val expectedUser = MadHatterUser(
      firstName = "Johnny",
      lastName = "Appleseed",
      email = "Johnny.Appleseed@usersc.com",
      amu = "badAmu")
    given(userRepository.getByEmail(anyString())).willReturn(expectedUser)

    createUserCommand.execute(expectedUser)
  }
}
