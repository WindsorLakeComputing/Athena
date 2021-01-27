package com.MadHatter.Athena.commands

import com.MadHatter.Athena.controllers.webModels.UserName
import org.springframework.context.annotation.Profile
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.stereotype.Service

interface UserNameSessionCommandInterface {
  fun execute(): UserName

}

@Service
@Profile("cloud")
class UserNameSessionCommand : UserNameSessionCommandInterface {
  override fun execute(): UserName
  {
    val authentication: OAuth2Authentication =
      SecurityContextHolder.getContext().authentication as OAuth2Authentication
    return UserName(authentication.userAuthentication.name)
  }
}

@Service
@Profile("test", "security_test", "local")
class TestUserNameSessionCommand  : UserNameSessionCommandInterface {
  override fun execute(): UserName
  {
    return UserName("Hermes Weasley")
  }
}
