package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.helpers.UserInfoFactory
import com.MadHatter.Athena.models.MadHatterUser
import com.MadHatter.Athena.models.UserInfo
import com.MadHatter.Athena.repositories.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
class UserInfoControllerTest {

  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var webApplicationContext: WebApplicationContext

  @get:Rule
  var restDocumentation = JUnitRestDocumentation()

  @get:Rule
  val wm = WireMockRule(5001)

  @Before
  fun setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
      .apply<DefaultMockMvcBuilder>(
        documentationConfiguration(restDocumentation)
          .operationPreprocessors()
          .withResponseDefaults(prettyPrint())
      )
      .build()
  }

  @MockBean
  lateinit var userRepository: UserRepository

  @Test
  fun `Get User Info`() {
    val expectedUserInfo = UserInfoFactory.create()
    val objectMapper = ObjectMapper()

    wm.start()

    WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/fake-sso-userinfo-endpoint-fake"))
      .withHeader("Authorization", WireMock.equalTo("We bare bearer tokens"))
      .willReturn(WireMock.aResponse()
        .withHeader("Content-Type", "application/json")
        .withBody(objectMapper.writeValueAsString(expectedUserInfo))))

    BDDMockito.given(userRepository.getByEmail(ArgumentMatchers.anyString()))
      .willReturn(MadHatterUser("First", "Last", "FUser@unknown.org", "BOLT"))

    mockMvc.perform(get("/userinfo")
      .header("Authorization", "We bare bearer tokens")
      .secure(true))
      .andExpect(MockMvcResultMatchers.status().isOk)
      .andDo(document("userinfo",
        responseFields(
          fieldWithPath("user_id").description("A number that looks like a guid that sso tile assigns everyone "),
          fieldWithPath("user_name").description("LDAP username ususally follows first inital last ex: bwatkins"),
          fieldWithPath("given_name").description("First Name and last name"),
          fieldWithPath("family_name").description("unknown.org"),
          fieldWithPath("name").description("given name and then family name"),
          fieldWithPath("email_verified").description("false"),
          fieldWithPath("email").description("username@unknown.org"),
          fieldWithPath("previous_logon_time").description("previous logon time"),
          fieldWithPath("sub").description("same as user_id"),
          fieldWithPath("amu").description("Bolt / Lightning")
          )))
  }
}
