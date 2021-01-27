package com.MadHatter.Athena

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler
import java.util.*

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
@Profile("cloud", "security_test")
class SecurityConfig : WebSecurityConfigurerAdapter() {
  public override fun configure(http: HttpSecurity) {
    http.authorizeRequests()
      .expressionHandler(OAuth2WebSecurityExpressionHandler())
      .antMatchers("/userinfo").permitAll()
      .antMatchers("/**").access("#oauth2.hasScope('madhatter.entity.read')")

    http.requiresChannel().anyRequest().requiresSecure()

    http.csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

    http.headers().contentSecurityPolicy(cspHeader())

    http.headers()
      .frameOptions().sameOrigin()
      .httpStrictTransportSecurity().disable()

    http.sessionManagement().maximumSessions(1)

    http.logout()
      .logoutSuccessUrl("/")
      .invalidateHttpSession(true)
      .clearAuthentication(true)
      .deleteCookies()
      .logoutSuccessHandler(CustomLogoutSuccessHandler())
  }
}

@Configuration
@Profile("test", "local")
class LocalSecurityConfig : WebSecurityConfigurerAdapter() {
  public override fun configure(http: HttpSecurity) {
    super.configure(http)

    val anonAuth = ArrayList<GrantedAuthority>()
    anonAuth.add(SimpleGrantedAuthority("ROLE_ANONYMOUS"))

    http
      .addFilter(AnonymousAuthenticationFilter("anonymous", "Hermes Weasley", anonAuth))
      .antMatcher("/**")
      .authorizeRequests()
      .anyRequest().permitAll()

    http.csrf()
      .disable()

    http.headers().frameOptions().disable()

    http.headers().contentSecurityPolicy(cspHeader())
  }
}

private fun cspHeader(): String{
  val cspJoiner = StringJoiner("; ")

  cspJoiner.add("default-src 'self'")
  cspJoiner.add("font-src 'self' 'report-sample'")
  cspJoiner.add("connect-src 'self'")
  cspJoiner.add("frame-src 'none'")
  cspJoiner.add("script-src 'self'")
  cspJoiner.add("frame-ancestors 'none'")
  cspJoiner.add("img-src 'self' data:")
  cspJoiner.add("style-src 'self' 'unsafe-inline'")

  return cspJoiner.toString()
}

@Configuration
@Profile("cloud", "security_test")
@EnableGlobalMethodSecurity(prePostEnabled = true)
class MethodSecurityConfig : GlobalMethodSecurityConfiguration() {
  override fun createExpressionHandler(): MethodSecurityExpressionHandler {
    return OAuth2MethodSecurityExpressionHandler()
  }
}
