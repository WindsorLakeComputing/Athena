package com.MadHatter.Athena

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = arrayOf(io.pivotal.spring.cloud.IssuerCheckConfiguration::class))
class AthenaApplication

fun main(args: Array<String>) {
  runApplication<AthenaApplication>(*args)
}