package com.MadHatter.Athena.controllers.security

import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class MockOAuth2AuthenticationToken: TestingAuthenticationToken {

  var mockName: String = ""

  constructor(principal: Any?, credentials: Any?,
              authorities: List<GrantedAuthority>, name: String) : super(principal, credentials, authorities) {
    this.mockName = name
  }
  @Override
  public override fun getName(): String {
    return mockName
  }
}