package com.MadHatter.Athena.controllers.security

import com.MadHatter.Athena.commands.GetMaintainerCommand
import com.MadHatter.Athena.helpers.MaintainerFactory
import com.MadHatter.Athena.commands.UpdateMaintainerCommand
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("security_test")
class MaintainerEndpointSecurityTest {

  val mapper = jacksonObjectMapper()

  @MockBean
  lateinit var getMaintainerCommand: GetMaintainerCommand

   @MockBean
   lateinit var updateMaintainerCommand: UpdateMaintainerCommand

  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var webApplicationContext: WebApplicationContext

  val file = MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE,
    "Hello, World!".toByteArray())

  @Before
  fun setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
      .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
      .build()
  }

  @Test
  @DisplayName("Hoot cannot get all maintainers")
  @WithMockOAuth2Scope(scopes = ["hoot.a.nanny"])
  fun hooterCantGetAllMaintainers() {
    val requestBuilder = get("/api/maintainers")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @DisplayName("Reader can get all maintainers")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read"])
  fun readerCanGetAllMaintainers() {
    val requestBuilder = get("/api/maintainers")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk)
  }

  @Test
  @DisplayName("Reader cannot update a maintainers")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read"])
  fun readerCantUpdateAMaintainer() {
    val requestBuilder = put("/api/maintainers/1")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isForbidden)
  }
  private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
  @Test
  @DisplayName("Editor can update a maintainer")
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read", "madhatter.athena.entity.edit"])
  fun editorCanUpdateAMaintainer() {
    val maintainer = MaintainerFactory.create(id = 1)

    mockMvc.perform(put("/api/maintainers/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(maintainer))
      .with(csrf())
      .secure(true))
      .andExpect(MockMvcResultMatchers.status().isOk)
  }

  @Test
  @DisplayName("Hoot cannot get maintainers")
  @WithMockOAuth2Scope(scopes = ["hoot.a.nanny"])
  fun hooterCantGetMaintainers() {
    val requestBuilder = get("/api/maintainers/1")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read"])
  fun `Read Scope can get a maintainer`() {
    val maintainer = MaintainerFactory.createPojo(id = 1)
    given(getMaintainerCommand.execute(1)).willReturn(maintainer)
    val requestBuilder = get("/api/maintainers/1")
      .secure(true)

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk)
  }

  @Test
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read"])
  fun `Read cannot upload maintainers with csv file`() {
    val requestBuilder = multipart("/api/maintainers/upload")
      .file(file)
      .secure(true)
      .with(csrf())

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isForbidden)
  }

  @Test
  @WithMockOAuth2Scope(scopes = ["madhatter.entity.read", "madhatter.athena.entity.create"])
  fun `Create can upload maintainers with csv file`() {
    val requestBuilder = multipart("/api/maintainers/upload")
      .file(file)
      .secure(true)
      .with(csrf())

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk)
  }
}
