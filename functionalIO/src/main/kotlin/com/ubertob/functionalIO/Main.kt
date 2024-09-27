package com.ubertob.functionalIO

import org.http4k.core.Method
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main() {
   val db = prepareDb()

    val userService = UserService()

    val app = routes(
        "/" bind Method.GET to { request -> userService.listUsers(request).run(db) },
        "/users" bind Method.POST to { request -> userService.addUser(request).run(db) },
        "/users/{id}" bind Method.GET to { request -> userService.getUserDetails(request).run(db)  }
    )

    app.asServer(Netty(8080)).start()
    println("Server started on http://localhost:8080")
}





