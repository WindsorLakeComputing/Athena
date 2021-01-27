package com.MadHatter.Athena.helpers

import com.MadHatter.Athena.models.UserInfo

object UserInfoFactory {
  fun create(
    user_id: String = "0dccc8e2-ac1c-4031-9008-7d745f58b3a2",
    user_name: String = "FUser",
    name: String = "Fake MadHatterUser unknown.org",
    given_name: String = "FUser",
    family_name: String = "unknown.org",
    email: String = "FUser@unknown.org",
    email_verified: Boolean = false,
    previous_logon_time: Long = 1556051070104,
    sub: String = "0dccc8e2-ac1c-4031-9008-7d745f58b3a2"
  ) = UserInfo(
    user_id,
    user_name,
    name,
    given_name,
    family_name,
    email,
    email_verified,
    previous_logon_time,
    sub
  )
}
