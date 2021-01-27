package com.MadHatter.Athena.minerva

import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import javax.persistence.*

data class RabbitMessages( var rabbitMessages: List<RabbitMessage> )

@Entity
data class RabbitMessage(
    var originQueue: String,
    var body: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long =0
    ) {
    @CreationTimestamp
    lateinit var createDate: Timestamp
}