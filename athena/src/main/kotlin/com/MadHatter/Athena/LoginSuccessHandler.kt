package com.MadHatter.Athena

import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component

@Component
class LogAuthenticationSuccess {
  private val kotlinLogger = KotlinLogging.logger {}

  @EventListener
  fun authenticated(event: AuthenticationSuccessEvent) {
    val username = event.authentication.principal.toString()
    kotlinLogger.info("logged in $username")
  }
}
