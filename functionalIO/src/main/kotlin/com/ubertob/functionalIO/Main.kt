package com.ubertob.functionalIO

import org.http4k.core.Method
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main() {
   val db = prepareDb()

    val userService = UserService(UserLoader(db), UserSaver(db), AllUserLoader(db))

    val app = routes(
        "/" bind Method.GET to userService::listUsers,
        "/users" bind Method.POST to userService::addUser,
        "/users/{id}" bind Method.GET to userService::getUserDetails
    )

    app.asServer(Netty(8080)).start()
    println("Server started on http://localhost:8080")
}





