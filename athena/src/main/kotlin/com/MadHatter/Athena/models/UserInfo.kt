package com.MadHatter.Athena.models

data class UserInfo  (
  val user_id: String,
  val user_name: String,
  val name: String,
  val given_name: String,
  val family_name: String,
  val email: String,
  val email_verified: Boolean,
  val previous_logon_time: Long,
  val sub: String
)
