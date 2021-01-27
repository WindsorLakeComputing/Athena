package com.MadHatter.Athena.controllers.webModels

data class NewAbsence(
    val maintainerId: Long,
    val startDate: String,
    val endDate: String,
    val reason: String,
    val location: String,
    val hours: String
)
