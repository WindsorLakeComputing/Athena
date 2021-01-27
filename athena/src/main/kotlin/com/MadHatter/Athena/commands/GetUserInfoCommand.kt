package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.AthenaUserInfo
import com.MadHatter.Athena.models.MadHatterUser
import com.MadHatter.Athena.models.MaintainerPojo
import com.MadHatter.Athena.models.UserInfo
import com.MadHatter.Athena.repositories.UserRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class GetUserInfoCommand {

  private val logger = KotlinLogging.logger {}

  @Value("\${security.oauth2.resource.user-info-uri}")
  lateinit var userInfoUri: String

  @Autowired
  lateinit var userRepository: UserRepository

  fun execute(authorizationToken: String?): ResponseEntity<AthenaUserInfo> {

    if(authorizationToken == null || authorizationToken.isEmpty()){
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    val headers = HttpHeaders()
    headers.set("Authorization", authorizationToken)

    val httpEntity = HttpEntity<MultiValueMap<String, String>>(headers)

    val restTemplate = RestTemplate()

    return try {
      val response: ResponseEntity<UserInfo> = restTemplate.exchange(userInfoUri, HttpMethod.GET, httpEntity, UserInfo::class.java)
      val responseBody: UserInfo? = response.body

      when (val madHatterUser: MadHatterUser? = userRepository.getByEmail(responseBody!!.email)) {
        is MadHatterUser -> {
          val newUserInfo = AthenaUserInfo(responseBody, madHatterUser.amu)
          ResponseEntity(newUserInfo, HttpStatus.OK)
        }

        else -> ResponseEntity(HttpStatus.NOT_FOUND)
      }
    } catch (httpClientException: HttpClientErrorException) {
      logger.warn(httpClientException.toString())
      ResponseEntity(httpClientException.statusCode)
    }
  }
}
