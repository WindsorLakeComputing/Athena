package com.MadHatter.Athena.models

data class MaintainerPojo constructor(
  val firstName: String,
  val lastName: String,
  val employeeId: String,
  val certificates: List<CertificatePojo>,
  val firstShiftPreference: Shift?,
  val secondShiftPreference: Shift?,
  val thirdShiftPreference: Shift?,
  val level: String,
  val rank: String,
  val section: Section,
  val shift: Shift?,
  val absences: List<AbsencePojo>?,
  val standbys: List<StandbyPojo>,
  val id: Long
) {
  constructor(maintainer: Maintainer) :
    this(
      maintainer.firstName,
      maintainer.lastName,
      maintainer.employeeId,
      maintainer.certificates.map { certificate -> CertificatePojo(certificate) },
      maintainer.firstShiftPreference,
      maintainer.secondShiftPreference,
      maintainer.thirdShiftPreference,
      maintainer.level,
      maintainer.rank,
      maintainer.section,
      maintainer.shift,
      maintainer.absences.map { absence -> AbsencePojo(absence) },
      maintainer.standbys.map { standBy -> StandbyPojo(standBy) },
      maintainer.id
      )
}
