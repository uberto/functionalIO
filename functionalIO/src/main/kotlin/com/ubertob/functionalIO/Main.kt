package com.ubertob.functionalIO

import org.http4k.core.Method
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main() {
    val db = prepareDb()

    with(db) {
        val app = routes(
            "/" bind Method.GET to { request -> listUsers(request) },
            "/users" bind Method.POST to { request -> addUser(request) },
            "/users/{id}" bind Method.GET to { request -> getUserDetails(request) }
        )


        app.asServer(Netty(8080)).start()
    }
    println("Server started on http://localhost:8080")
}





