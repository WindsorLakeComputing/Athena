package com.MadHatter.Athena.commands

import com.MadHatter.Athena.helpers.AthenaUserInfoFactory
import com.MadHatter.Athena.models.MadHatterUser
import com.MadHatter.Athena.repositories.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.unauthorized
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import java.util.*


@RunWith(SpringRunner::class)
class GetUserInfoCommandTest {
  @TestConfiguration
  internal class EmployeeServiceImplTestContextConfiguration {

    @Bean
    fun reallyFun(): GetUserInfoCommand {
      return GetUserInfoCommand()
    }
  }

  @get:Rule
  val wm = WireMockRule(5001)

  @Before
  internal fun beforeEach() {
    wm.start()
    ReflectionTestUtils.setField(getUserInfoCommand,
      "userInfoUri",
      "http://localhost:5001/fake-sso-userinfo-endpoint-fake")
  }

  @After
  internal fun afterEach() {
    wm.stop()
  }

  @Autowired
  lateinit var getUserInfoCommand: GetUserInfoCommand

  @MockBean
  lateinit var userRepository: UserRepository

  val objectMapper = ObjectMapper()

  @Test
  fun `getUserInfo returns response entity with 200, user info from SSO plus user's AMU when given a valid token`() {
    given(userRepository.getByEmail(anyString()))
      .willReturn(MadHatterUser("First", "Last", "FUser@unknown.org", "FAKE AMU"))

    stubFor(get(urlEqualTo("/fake-sso-userinfo-endpoint-fake"))
      .withHeader("Authorization", containing("authToken"))
      .willReturn(aResponse()
        .withHeader("Content-Type", "application/json")
        .withStatus(200)
        .withBody(objectMapper.writeValueAsString(UserInfoReturnedFromSSO()))))

    val expectedUserInfo = AthenaUserInfoFactory.create(amu = "FAKE AMU")
    val response = getUserInfoCommand.execute("authToken")

    assertEquals(expectedUserInfo, response.body)
    assertEquals(HttpStatus.OK, response.statusCode)

    verify(userRepository, times(1)).getByEmail(anyString())
  }

  @Test
  fun `getUserInfo returns a response entity with the associated error code when it receives a client error`() {
    stubFor(get(urlEqualTo("/fake-sso-userinfo-endpoint-fake"))
      .withHeader("Authorization", containing("authToken"))
      .willReturn(unauthorized()
        .withHeader("Content-Type", "application/json")))

    val response = getUserInfoCommand.execute("authToken")

    assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
  }

  @Test
  fun `getUserInfo returns a response entity with the bad request if no token sent`() {
    val response = getUserInfoCommand.execute("")

    assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
  }
}

data class UserInfoReturnedFromSSO (
  var user_id: String = "0dccc8e2-ac1c-4031-9008-7d745f58b3a2",
  var user_name: String = "FUser",
  var name: String = "Fake AthenaUserInfo unknown.org",
  var given_name: String = "FUser",
  var family_name: String = "unknown.org",
  var email: String = "FUser@unknown.org",
  var email_verified: Boolean = false,
  var previous_logon_time: Long = 1556051070104,
  var sub: String = "0dccc8e2-ac1c-4031-9008-7d745f58b3a2"
)
