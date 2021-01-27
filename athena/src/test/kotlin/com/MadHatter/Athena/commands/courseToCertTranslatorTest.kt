package com.MadHatter.Athena.commands

import org.hamcrest.Matchers.contains
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class CourseToCertTranslatorTest {

  @Test
  fun `Test All Course to Cert Mappings`() {
    val courseToCertMaps = listOf(
      Pair(listOf("I/E"), listOf("002052")),
      Pair(listOf("HP"), listOf("002260","002277")),
      Pair(listOf("TS"), listOf("002431")),
      Pair(listOf("Run"), listOf("002398", "002435", "002931", "002932")),
      Pair(listOf("Bore"), listOf("005101", "002435", "005155", "005156"))
    )

    courseToCertMaps.forEach{ testMap(it) }
  }

  private fun testMap(map: Pair<List<String>, List<String>>) {
    val certs = CourseToCertTranslator.getCerts(map.second)

    assertEquals("for ${map.first}, expected ${map.first.size} certs, but got ${certs.size}", map.first.size, certs.size)
    map.first.map{ cert -> assertThat("expected ${map.first} to contain ${cert}, but it didnt", certs, contains(cert))}
  }

  @Test
  fun `Given course nums for many certs, Returns many certs`() {
    val certs = CourseToCertTranslator.getCerts(listOf("002431", "002277", "002260"))

    assertEquals(2, certs.size)
    assertTrue(certs.contains("HP"))
    assertTrue(certs.contains("TS"))
  }

  @Test
  fun `Disregards course codes that dont map to certs`() {
    val certs = CourseToCertTranslator.getCerts(listOf("Monkey Punch", "Chicken Scratch", "Donkey Kick"))

    assertEquals(0, certs.size)
  }
}
