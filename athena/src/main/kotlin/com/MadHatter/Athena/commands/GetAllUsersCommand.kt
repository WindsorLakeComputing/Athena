package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.MadHatterUser
import com.MadHatter.Athena.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetAllUsersCommand {

  @Autowired
  lateinit var userRepository: UserRepository

  fun execute(): List<MadHatterUser> = userRepository.findAll()
}
