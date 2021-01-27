package com.MadHatter.Athena.models

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "absence")
data class Absence(

  var startDate: LocalDate,

  var endDate: LocalDate,

  @ManyToOne
  @JoinColumn(name = "maintainer_id")
  var maintainer: Maintainer,

  var reason: String,

  var location: String,

  var hours: String,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0

) {
  init {
    val reasonRegex = "^[0-9A-Za-z ?!.]{1,20}$".toRegex()
    require(reason.matches(reasonRegex)){
      "Invalid Reason"
    }
  }
}
