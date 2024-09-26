package com.ubertob.functionalIO

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 100)
    override val primaryKey = PrimaryKey(id)
}

class UserLoader(val db: Database) : (UserId) -> User? {
    override fun invoke(userId: UserId): User? = transaction(db) {
        Users.select { Users.id eq userId }.map {
            User(it[Users.id], it[Users.name], it[Users.email])
        }.singleOrNull()
    }
}

class UserSaver(val db: Database) : (String, String) -> UserId {
    override fun invoke(uName: String, uEmail: String): UserId =
        transaction(db) { //Database should be passed explicitly !!!
            Users.insert {
                it[name] = uName
                it[email] = uEmail
            } get Users.id
        }
}

class AllUserLoader(val db: Database) : () -> List<User> {
    override fun invoke(): List<User> = transaction(db) {
        Users.selectAll().map { User(it[Users.id], it[Users.name], it[Users.email]) }
    }
}


fun prepareDb(): Database {
    val db = Database.connect("jdbc:sqlite:users.db", "org.sqlite.JDBC")
    transaction(db) {
        SchemaUtils.create(Users)
        Users.deleteAll()
    }

    return db
}