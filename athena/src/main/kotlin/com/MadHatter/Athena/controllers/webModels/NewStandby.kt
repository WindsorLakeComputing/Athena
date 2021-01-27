package com.MadHatter.Athena.controllers.webModels

data class NewStandby(
  val maintainerId: Long,
  val startDate: String,
  val endDate: String
)