package com.MadHatter.Athena.models

import com.MadHatter.Athena.helpers.MaintainerFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class MaintainerTest {
  @Test
  fun createMaintainerTest() {
    val subject = MaintainerFactory.create()

    assertEquals(subject.firstName, "Curtis-'")
    assertEquals(subject.lastName, "LeMay")
    assertEquals(subject.employeeId, "1234")
    assertEquals(subject.level, "3")
    assertEquals(subject.rank, "E2")
    assertEquals(subject.shift, Shift("S"))
  }

  @Test
  fun createMaintainerWithNumberInFirstNameTest() {
    try {
      MaintainerFactory.create(firstName = "JAMES9")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid First Name")
    }
  }

  @Test
  fun createMaintainerWithMoreThan100CharactersInFirstName() {
    try {
      MaintainerFactory.create(firstName = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwyxz")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid First Name")
    }
  }

  @Test
  fun createMaintainerWithNumberInLastNameTest() {
    try {
      MaintainerFactory.create(lastName = "JAMES9")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid Last Name")
    }
  }

  @Test
  fun createMaintainerWithMoreThan100CharactersInLastName() {
    try {
      MaintainerFactory.create(lastName = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwyxz")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid Last Name")
    }
  }

  @Test
  fun createMaintainerEmployeeNumberOnlyAllowsDigits() {
    try {
      MaintainerFactory.create(employeeId = "12345ABCD")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid Employee Id")
    }
  }

  @Test
  fun createMaintainerEmployeeNumberMax10Digits() {
    try {
      MaintainerFactory.create(employeeId = "12345678901")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid Employee Id")
    }
  }

  @Test
  fun createMaintainerLevelMax1Digits() {
    try {
      MaintainerFactory.create(level = "12")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid Level")
    }
  }

  @Test
  fun createMaintainerLevelOnlyAllowsDigits() {
    try {
      MaintainerFactory.create(level = "a")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid Level")
    }
  }

  @Test
  fun createMaintainerRankAllowsOnlyAlphanumericCharacters() {
    try {
      MaintainerFactory.create(rank = "Mjr. Pain")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid Rank")
    }
  }

  @Test
  fun createMaintainerRankAllowsOnly10Characters() {
    try {
      MaintainerFactory.create(rank = "ABCDEFGHIJKLMNOP")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals(e.message, "Invalid Rank")
    }
  }
}
