package com.MadHatter.Athena.models

import com.MadHatter.Athena.helpers.MaintainerFactory
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate

@RunWith(SpringRunner::class)
class AbsenceTest {
  @Test
  fun failsToCreateAbsenceWhenReasonHasMoreThan20Characters () {
    try {
      val maintainer = MaintainerFactory.create()
      Absence(
        LocalDate.now(),
        LocalDate.MAX,
        maintainer,
        "PartyPartyPartyPartyP",
        "",
        "")
      Assert.fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      Assert.assertEquals(e.message, "Invalid Reason")
    }
  }

  @Test
  fun failsToCreateAbsenceWhenReasonHasInvalidCharacters () {
    try {
      val maintainer = MaintainerFactory.create()
      Absence(
        LocalDate.now(),
        LocalDate.MAX,
        maintainer,
        "<>[]{}()=+",
        "",
        "")
      Assert.fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      Assert.assertEquals(e.message, "Invalid Reason")
    }
  }
}
