package com.MadHatter.Athena.repositories

import com.MadHatter.Athena.models.Maintainer
import org.springframework.data.jpa.repository.JpaRepository

interface MaintainerRepository : JpaRepository<Maintainer, Long> {

  fun getByLastName(lastName: String): List<Maintainer>

  fun getByFirstNameAndLastNameAndEmployeeId(
    firstName: String,
    lastName: String,
    employeeId: String
  ): Maintainer?
}