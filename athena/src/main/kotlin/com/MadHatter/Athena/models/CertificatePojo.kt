package com.MadHatter.Athena.models

data class CertificatePojo constructor(
  val name: String,
  val id: Long
){
  constructor(certificate: Certificate):
    this(
      certificate.name,
      certificate.id
    )
}