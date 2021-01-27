package com.MadHatter.Athena.models

import javax.persistence.*

@Entity
@Table(name = "maintainer")
data class Maintainer(

  var firstName: String,

  var lastName: String,

  var employeeId: String,

  @ManyToMany
  @JoinTable(
    name = "maintainer_certificates",
    joinColumns = [JoinColumn(name = "maintainer_id")],
    inverseJoinColumns = [JoinColumn(name = "certificate_id")]
  )
  var certificates: List<Certificate> = listOf(),

  @ManyToOne
  var firstShiftPreference: Shift? = null,

  @ManyToOne
  var secondShiftPreference: Shift? = null,

  @ManyToOne
  var thirdShiftPreference: Shift? = null,

  var level: String,

  var rank: String,

  @OneToOne
  var section: Section,

  @OneToOne
  var shift: Shift? = null,

  @OneToMany(mappedBy = "maintainer", cascade = [CascadeType.ALL], orphanRemoval = true)
  var absences: List<Absence> = listOf(),

  @OneToMany(mappedBy = "maintainer", cascade = [CascadeType.ALL], orphanRemoval = true)
  var standbys: List<Standby> = listOf(),

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0

) {
  init {
    val nameRegex = "^[A-Za-z' -]{1,100}$".toRegex()
    val idRegex = "^[0-9]{1,10}$".toRegex()
    val rankRegex = "^[A-Za-z0-9]{1,10}$".toRegex()
    val levelRegex = "^[13579]$".toRegex()
    require(firstName.matches(nameRegex)) {
      "Invalid First Name"
    }
    require(lastName.matches(nameRegex)) {
      "Invalid Last Name"
    }
    require(employeeId.matches(idRegex)) {
      "Invalid Employee Id"
    }
    require(level.matches(levelRegex)) {
      "Invalid Level"
    }
    require(rank.matches(rankRegex)) {
      "Invalid Rank"
    }
  }

  object Sort : Comparator<Maintainer> {
    override fun compare(a: Maintainer, b: Maintainer): Int {
      val shiftMap: HashMap<String, Int> = hashMapOf(
        Pair("D", 1),
        Pair("S", 2),
        Pair("M", 3),
        Pair("NOT", 9)
      )

      val shift = shiftMap[a.shift?.name ?: "NOT"]!! - shiftMap[b.shift?.name ?: "NOT"]!!
      if (shift!=0)
        return shift

      val level = b.level.toInt() - a.level.toInt()
      if (level!=0)
        return level

      return a.lastName.compareTo(b.lastName)
    }
  }
}

