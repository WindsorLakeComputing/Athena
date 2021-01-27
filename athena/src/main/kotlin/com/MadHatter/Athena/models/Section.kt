package com.MadHatter.Athena.models

import javax.persistence.*

@Entity
@Table(name="section")
data class Section (
  var name: String,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = 0
)
{
  init {
    require(name.matches("Weapons|APG|Production|Support|Specialists".toRegex())){
      "Invalid Section"
    }
  }
}