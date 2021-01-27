package com.MadHatter.Athena.models

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class ShiftTest {
  @Test
  fun createShiftDoesNotAllowInvalidShifts() {
    val shiftValues = listOf("Z", "NOTVALID", "q", "7", "ç≈√©®∂∫˜˚")

    shiftValues.forEach { shift ->
      try {
        Shift(shift)
        fail("Exception Expected")
      } catch (e: IllegalArgumentException) {
        assertEquals(e.message, "Invalid Shift")
      }
    }
  }

  @Test
  fun createShiftAllowsValidShifts() {
    val shiftValues = listOf("D", "S", "M")

    shiftValues.forEach { shift ->
      try {
        val newShift = Shift(shift)
        assertEquals(newShift.name, shift)
      } catch (e: IllegalArgumentException) {
        fail("Failed to create Shift: $shift")
      }
    }
  }
}