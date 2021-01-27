package com.MadHatter.Athena.helpers

import com.MadHatter.Athena.controllers.security.MockOAuth2AuthenticationToken
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2Request
import java.io.Serializable
import java.util.*

object AuthenticationTestHelpers {

  fun getMockAuthentication(name: String, scope: String = ""): Authentication {
    val authorities = AuthorityUtils.createAuthorityList("ROLE_RAPTORIAN")

    val details = LinkedHashMap<String, String>()

    val token = MockOAuth2AuthenticationToken(null, null, authorities, name)
    token.details = details

    return OAuth2Authentication(getMockOauth2Request(scope), token)
  }

  fun getMockOauth2ClientContext(): OAuth2ClientContext {
    val mockClient = Mockito.mock(OAuth2ClientContext::class.java)
    given(mockClient.accessToken).willReturn(DefaultOAuth2AccessToken("my-fun-token"))

    return mockClient
  }

  private fun getMockOauth2Request(scope: String): OAuth2Request {
    val requestParameters = mapOf<String, String>()
    val clientId = "oauth-client-id"
    val authorities = AuthorityUtils.createAuthorityList("Everything")
    val approved = true
    val scopes = setOf(scope)
    val resourceIds = setOf<String>()
    val redirectUrl = "http://my-redirect-url.com"
    val responseTypes = setOf<String>()
    val extensionProperties = mapOf<String, Serializable>()

    return OAuth2Request(
      requestParameters,
      clientId,
      authorities,
      approved,
      scopes,
      resourceIds,
      redirectUrl,
      responseTypes,
      extensionProperties
    )
  }
}
