package com.MadHatter.Athena.models

import javax.persistence.*

@Entity
@Table(name="certificate")
data class Certificate(
  var name: String,

  @ManyToMany(mappedBy = "certificates")
  var maintainers: Set<Maintainer> = setOf(),

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0

)