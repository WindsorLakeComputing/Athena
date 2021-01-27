package com.MadHatter.Athena.models

data class AthenaUserInfo (
  val user_id: String,
  val user_name: String,
  val name: String,
  val given_name: String,
  val family_name: String,
  val email: String,
  val email_verified: Boolean,
  val previous_logon_time: Long,
  val sub: String,
  val amu: String
) {
  constructor(userInfo: UserInfo, amu: String) : this(
    userInfo.user_id,
    userInfo.user_name,
    userInfo.name,
    userInfo.given_name,
    userInfo.family_name,
    userInfo.email,
    userInfo.email_verified,
    userInfo.previous_logon_time,
    userInfo.sub,
    amu
  )
}
