package com.MadHatter.Athena.models

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class MadHatterUserTest {
  @Test
  fun `create MadHatterUser`() {
    val subject = MadHatterUserFactory.create()

    assertEquals(subject.firstName, "bob")
    assertEquals(subject.lastName, "dole")
    assertEquals(subject.email, "bob.dole@us.mil")
  }

  @Test
  fun `does not create a MadHatterUser from an email without @`() {
    try {
      MadHatterUserFactory.create(email = "bob.dole")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals("Invalid email address", e.message)
    }
  }

  @Test
  fun `does not create a MadHatterUser from a email with more than 100 characters`() {
    val longEmail = "abcdefghijklmnopqrstuvwxyz" +
      "abcdefghijklmnopqrstuvwxyz" +
      "@" +
      "abcdefghijklmnopqrstuvwxyz" +
      "abcdefghijklmnopqrstuvwxyz"

    try {
      MadHatterUserFactory.create(email = longEmail)
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals("Invalid email address", e.message)
    }
  }

  @Test
  fun `does not create a MadHatterUser from a firstName with illegal characters`(){
    try {
      MadHatterUserFactory.create(firstName = "BADNAME2345%^&")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals("Invalid First Name", e.message)
    }
  }

  @Test
  fun `does not create a MadHatterUser from a firstName with more than 100 characters`() {
    val longFirstName = "abcdefghijklmnopqrstuvwxyz" +
      "abcdefghijklmnopqrstuvwxyz" +
      "abcdefghijklmnopqrstuvwxyz" +
      "abcdefghijklmnopqrstuvwxyz"

    try {
      MadHatterUserFactory.create(firstName = longFirstName)
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals("Invalid First Name", e.message)
    }
  }

  @Test
  fun `does not create a MadHatterUser from a lastName with illegal characters`() {
    try {
      MadHatterUserFactory.create(lastName = "BADNAMES2345*@*@")
      fail("Exception Expected")
    } catch (e: IllegalArgumentException) {
      assertEquals("Invalid Last Name", e.message)
    }
  }
}

object MadHatterUserFactory {
  fun create(firstName: String = "bob",
             lastName: String = "dole",
             email: String = "bob.dole@us.mil",
             amu: String = "badAmu")
    = MadHatterUser(firstName, lastName, email, amu)
}
