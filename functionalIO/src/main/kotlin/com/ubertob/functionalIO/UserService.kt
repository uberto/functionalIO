package com.ubertob.functionalIO

import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.routing.path
import org.jetbrains.exposed.sql.Database

class UserService(val database: Database) {

    fun listUsers(request: Request): Response {
        val users = loadAllUsers(database)
        return Response(OK).body(renderUserList(users))
    }

    fun addUser(request: Request): Response {
        val formData = request.form().toParametersMap()
        val name = formData.getFirst("name") ?: return Response(Status.BAD_REQUEST)
        val email = formData.getFirst("email") ?: return Response(Status.BAD_REQUEST)

        saveUser(database, name, email)

        return Response(Status.SEE_OTHER).header("Location", "/")
    }

    fun getUserDetails(request: Request): Response {
        val id = request.path("id")?.toIntOrNull() ?: return Response(Status.BAD_REQUEST)
        val user = loadUser(database, id)

        return if (user != null) {
            Response(Status.OK).body(renderUserDetails(user))
        } else {
            Response(Status.NOT_FOUND).body("User not found")
        }
    }
}



