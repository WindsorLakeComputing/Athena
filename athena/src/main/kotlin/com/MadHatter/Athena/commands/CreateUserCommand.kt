package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.MadHatterUser
import com.MadHatter.Athena.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreateUserCommand {

  @Autowired
  lateinit var userRepository: UserRepository

  fun execute(user: MadHatterUser): MadHatterUser? =
    when (userRepository.getByEmail(user.email)) {
      is MadHatterUser -> throw Exception()
      else -> userRepository.save(user)
  }
}
