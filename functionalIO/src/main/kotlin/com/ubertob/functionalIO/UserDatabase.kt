package com.ubertob.functionalIO

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 100)
    override val primaryKey = PrimaryKey(id)
}

fun saveUser(uName: String, uEmail: String) = transaction {
    Users.insert {
        it[name] = uName
        it[email] = uEmail
    }
}


fun loadUser(id: Int): User? =
    Users.select { Users.id eq id }.map { User(it[Users.id], it[Users.name], it[Users.email]) }.singleOrNull()


fun loadAllUsers(): List<User> =
    Users.selectAll().map { User(it[Users.id], it[Users.name], it[Users.email]) }


fun prepareDb() {
    Database.connect("jdbc:sqlite:users.db", "org.sqlite.JDBC")
    transaction {
        SchemaUtils.create(Users)
        saveUser("John Smith", "jsmith@acme.com")
        saveUser("Betty Page", "bpage@acme.com")
    }
}