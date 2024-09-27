package com.ubertob.functionalIO

import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.routing.path

//Functional Hexagonal,Ports and Adapters
class UserService() {

    fun listUsers(request: Request): DbReader<Response> =
        DbReader { AllUserLoader(it)() }
            .transform(::renderUserList)
            .transform { Response(OK).body(it) }


    fun addUser(request: Request): DbReader<Response> {

        val formData = request.form().toParametersMap()
        val name = formData.getFirst("name")
        val email = formData.getFirst("email")

        return if (name.isNullOrBlank() || email.isNullOrBlank()) {
            DbReader { Response(Status.BAD_REQUEST) }
        } else {
            DbReader {
                UserSaver(it)(name, email)
            }.transform {
                Response(Status.SEE_OTHER).header("Location", "/")
            }
        }
    }

    fun getUserDetails(request: Request): DbReader<Response> {
        val id = request.path("id")?.toIntOrNull()
        return if (id == null) {
            DbReader { Response(Status.BAD_REQUEST) }
        } else {
            DbReader {
                UserLoader(it)(id)
            }.transform { user ->
                if (user != null) {
                    Response(Status.OK).body(renderUserDetails(user))
                } else {
                    Response(Status.NOT_FOUND).body("User not found")
                }
            }
        }
    }
}



