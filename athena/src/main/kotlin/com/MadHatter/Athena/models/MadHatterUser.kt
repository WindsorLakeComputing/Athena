package com.MadHatter.Athena.models

import javax.persistence.*

@Entity
@Table(name="users")
data class MadHatterUser (

  var firstName: String,
  var lastName: String,
  var email: String,
  val amu: String,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = 0
) {
  init {
    require(email.matches(".*@.*".toRegex())) {"Invalid email address"}
    require(email.length < 100) { "Invalid email address"}

    val nameRegex = "^[A-Za-z' -]{1,100}$".toRegex()
    require(firstName.matches(nameRegex)) {"Invalid First Name"}
    require(lastName.matches(nameRegex)) {"Invalid Last Name"}
  }
}
