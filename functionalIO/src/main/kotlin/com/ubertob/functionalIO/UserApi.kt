package com.ubertob.functionalIO

import java.io.File

typealias UserId = Int
typealias Html = String

fun userPage(userId: UserId): Html {
    val repository = UserDatabase.inMemory()
    val user = repository.findUser(userId)
    val htmlTemplate = File("userTemplate.html").readLines()

    return htmlTemplate
        .joinToString()
        .replace("{user.name}", user?.name ?: "")
}

