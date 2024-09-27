package com.ubertob.functionalIO

import org.jetbrains.exposed.sql.Database

data class DbReader<T>(val fn: (Database) -> T) {

    fun <U> transform(ufn: (T) -> U): DbReader<U> = DbReader { ufn(fn(it)) }
    fun run(db: Database): T = fn(db)
}