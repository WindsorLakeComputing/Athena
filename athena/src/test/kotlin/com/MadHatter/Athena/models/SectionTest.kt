package com.MadHatter.Athena.models

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class SectionTest {
  @Test
  fun createSectionDoesNotAllowInvalidSections() {
    val sectionValues = listOf("Z", "NOTVALID", "q", "7", "ç≈√©®∂∫˜˚")

    sectionValues.forEach { section ->
      try {
        Section(section)
        fail("Exception Expected")
      } catch (e: IllegalArgumentException) {
        assertEquals(e.message, "Invalid Section")
      }
    }
  }

  @Test
  fun createSectionAllowsValidSections() {
    val sectionValues = listOf(
      "Weapons",
      "APG",
      "Production",
      "Support",
      "Specialists"
    )

    sectionValues.forEach { section ->
      try {
        val newSection = Section(section)
        assertEquals(newSection.name, section)
      } catch (e: IllegalArgumentException) {
        fail("Failed to create Section: $section")
      }
    }
  }
}