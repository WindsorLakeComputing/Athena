package com.MadHatter.Athena.helpers

import com.MadHatter.Athena.models.*


object MaintainerFactory {
  fun create(
    firstName: String = "Curtis-'",
    lastName: String = "LeMay",
    employeeId: String = "1234",
    certificates: List<Certificate> = listOf(),
    firstShiftPreference: Shift = Shift("D"),
    secondShiftPreference: Shift = Shift("M"),
    thirdShiftPreference: Shift = Shift("S"),
    level: String = "3",
    rank: String = "E2",
    section: Section = Section("APG"),
    shift: Shift = Shift("S"),
    id: Long = 0
  ) = Maintainer(
    firstName,
    lastName,
    employeeId,
    certificates,
    firstShiftPreference,
    secondShiftPreference,
    thirdShiftPreference,
    level,
    rank,
    section,
    shift,
    mutableListOf(),
    mutableListOf(),
    id
  )

  fun createPojo(
    firstName: String = "Curtis-'",
    lastName: String = "LeMay",
    employeeId: String = "1234",
    certificates: List<CertificatePojo> = listOf(),
    firstShiftPreference: Shift = Shift("D"),
    secondShiftPreference: Shift = Shift("M"),
    thirdShiftPreference: Shift = Shift("S"),
    level: String = "3",
    rank: String = "E2",
    section: Section = Section("APG"),
    shift: Shift = Shift("S"),
    id: Long = 0
  ) = MaintainerPojo(
    firstName,
    lastName,
    employeeId,
    certificates,
    firstShiftPreference,
    secondShiftPreference,
    thirdShiftPreference,
    level,
    rank,
    section,
    shift,
    mutableListOf(),
    mutableListOf(),
    id
  )
}
