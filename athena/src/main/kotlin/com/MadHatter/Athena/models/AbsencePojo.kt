package com.MadHatter.Athena.models

import java.time.LocalDate

data class AbsencePojo constructor(
  val startDate: LocalDate,
  val endDate: LocalDate,
  val reason: String,
  val location: String,
  val id: Long,
  val hours: String
){
  constructor(absence:Absence):
    this(
      absence.startDate,
      absence.endDate,
      absence.reason,
      absence.location,
      absence.id,
      absence.hours)
}
