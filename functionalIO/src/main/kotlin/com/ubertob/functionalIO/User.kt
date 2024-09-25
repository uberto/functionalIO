package com.ubertob.functionalIO

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 100)
    override val primaryKey = PrimaryKey(id)
}

data class User(val id: Int, val name: String, val email: String)
