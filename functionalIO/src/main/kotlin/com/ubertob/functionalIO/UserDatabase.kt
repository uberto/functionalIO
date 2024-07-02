package com.ubertob.functionalIO

class UserDatabase(private val users: List<User>) {
    fun findUser(userId: UserId): User? = users.firstOrNull { it.id == userId }

    companion object {
        fun inMemory(): UserDatabase =
            UserDatabase(
                listOf(
                    User(1, "John Smith"),
                    User(2, "Betty Page")
                )
            )
    }

}