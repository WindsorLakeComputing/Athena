package com.MadHatter.Athena.commands

object CourseToCertTranslator {
  private val certToCourseCodesMap: HashMap<String, List<String>> = hashMapOf(
      Pair("I/E", listOf("002052")),
      Pair("HP", listOf("002260","002277")),
      Pair("TS", listOf("002431")),
      Pair("Run", listOf("002398", "002435", "002931", "002932")),
      Pair("Bore", listOf("005101", "005155", "005156"))
  )

  fun getCerts(courseCodes: List<String>): Set<String>
    = certToCourseCodesMap.filterValues{ value -> courseCodes.containsAll(value)}.keys
}
