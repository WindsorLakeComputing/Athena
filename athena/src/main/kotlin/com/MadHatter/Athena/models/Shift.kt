package com.MadHatter.Athena.models

import javax.persistence.*

@Entity
@Table(name="shift")
data class Shift(
  var name: String,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = 0
) {
  init {
    require(name.matches("[DMS]".toRegex())) {
      "Invalid Shift"
    }
  }
}
