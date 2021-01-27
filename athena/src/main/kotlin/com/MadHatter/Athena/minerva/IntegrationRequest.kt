package com.MadHatter.Athena.minerva

import java.sql.Timestamp
import javax.persistence.*

data class IntegrationRequests( var integrationRequests: List<IntegrationRequest> )

@Entity
data class IntegrationRequest(
    var timestamp: Timestamp,
    var errorMessage: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
)
