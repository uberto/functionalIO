package com.ubertob.functionalIO

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 100)
    override val primaryKey = PrimaryKey(id)
}


fun saveUser(db: Database, uName: String, uEmail: String): UserId =
    transaction(db) { //Database should be passed explicitly !!!
        Users.insert {
            it[name] = uName
            it[email] = uEmail
        } get Users.id
    }

fun loadUser(db: Database, id: UserId): User? =
    transaction(db) {
        Users.select { Users.id eq id }.map { User(it[Users.id], it[Users.name], it[Users.email]) }.singleOrNull()
    }

fun loadAllUsers(db: Database): List<User> =
    transaction(db) {
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