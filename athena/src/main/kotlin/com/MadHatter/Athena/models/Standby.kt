package com.MadHatter.Athena.models

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name="standby")
data class Standby (

  var startDate: LocalDate,

  var endDate: LocalDate,

  @ManyToOne
  @JoinColumn(name = "maintainer_id")
  var maintainer: Maintainer,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = 0

)