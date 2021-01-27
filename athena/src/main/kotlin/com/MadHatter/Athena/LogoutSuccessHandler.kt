package com.MadHatter.Athena

import mu.KotlinLogging
import java.io.IOException
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomLogoutSuccessHandler : SimpleUrlLogoutSuccessHandler(), LogoutSuccessHandler {
  private val kotlinLogger = KotlinLogging.logger {}

  @Throws(IOException::class, ServletException::class)
  override fun onLogoutSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication?) {

    if(authentication != null){
      kotlinLogger.info("logged out " + authentication.name)
    } else {
      kotlinLogger.info("Attempted to logout without authentication available!")
    }

    super.onLogoutSuccess(request, response, authentication)
  }
}
