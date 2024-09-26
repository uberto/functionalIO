package com.ubertob.functionalIO

import org.jetbrains.exposed.sql.Database

data class DbReader<T>(val fn: (Database) -> T ) {

    fun run(db: Database): T = fn(db)
}