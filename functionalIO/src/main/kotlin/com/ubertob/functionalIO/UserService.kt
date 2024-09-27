package com.ubertob.functionalIO

import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.routing.path
import org.jetbrains.exposed.sql.Database


context (Database)
fun listUsers(request: Request): Response {
    val users = loadAllUsers()

    return Response(OK).body(renderUserList(users))
}


context (Database)
fun addUser(request: Request): Response {

    val formData = request.form().toParametersMap()
    val name = formData.getFirst("name")
    val email = formData.getFirst("email")

    return if (name.isNullOrBlank() || email.isNullOrBlank()) {
        Response(Status.BAD_REQUEST)
    } else {

        saveUser(name, email)

        Response(Status.SEE_OTHER).header("Location", "/")
    }
}


context (Database)
fun getUserDetails(request: Request): Response {
    val id = request.path("id")?.toIntOrNull()
    return if (id == null) {
        Response(Status.BAD_REQUEST)
    } else {
        val user = loadUser(id)
        if (user != null) {
            Response(Status.OK).body(renderUserDetails(user))
        } else {
            Response(Status.NOT_FOUND).body("User not found")
        }
    }
}




