package com.MadHatter.Athena.models

import java.time.LocalDate

data class StandbyPojo constructor(
  val startDate: LocalDate,
  val endDate: LocalDate,
  val id: Long?
){
  constructor(standby: Standby):
    this(
      standby.startDate,
      standby.endDate,
      standby.id
    )
}
