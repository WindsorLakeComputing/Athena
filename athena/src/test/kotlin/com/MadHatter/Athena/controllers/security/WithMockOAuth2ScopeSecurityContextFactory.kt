package com.MadHatter.Athena.controllers.security

import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2Request
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory


class WithMockOAuth2ScopeSecurityContextFactory : WithSecurityContextFactory<WithMockOAuth2Scope> {

  override fun createSecurityContext(mockOAuth2Scope: WithMockOAuth2Scope): SecurityContext {
    val context = SecurityContextHolder.createEmptyContext()

    var scope = mockOAuth2Scope.scopes.toSet()

    val request = OAuth2Request(null, null, null, true, scope, null, null, null, null)

    val auth = OAuth2Authentication(request, null)

    context.authentication = auth

    return context
  }
}