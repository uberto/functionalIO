package com.ubertob.functionalIO

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 100)
    override val primaryKey = PrimaryKey(id)
}


fun Database.loadUser(userId: UserId): User? = transaction(this) {
    Users.select { Users.id eq userId }.map {
        User(it[Users.id], it[Users.name], it[Users.email])
    }.singleOrNull()
}

fun Database.saveUser(uName: String, uEmail: String): UserId =
    transaction(this) { //Database should be passed explicitly !!!
        Users.insert {
            it[name] = uName
            it[email] = uEmail
        } get Users.id
    }


fun Database.loadAllUsers(): List<User> = transaction(this) {
    Users.selectAll().map { User(it[Users.id], it[Users.name], it[Users.email]) }
}


fun prepareDb(): Database {
    val db = Database.connect("jdbc:sqlite:users.db", "org.sqlite.JDBC")
    transaction(db) {
        SchemaUtils.create(Users)
        Users.deleteAll()
    }

    return db
}