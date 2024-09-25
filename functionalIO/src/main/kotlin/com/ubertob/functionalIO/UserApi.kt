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



fun listUsers(request: Request): Response {
    val users = transaction {
        Users.selectAll().map { User(it[Users.id], it[Users.name], it[Users.email]) }
    }
    return Response(Status.OK).body(renderUserList(users))
}

fun addUser(request: Request): Response {
    val formData = request.form()
    val name = formData?.get("name") ?: return Response(Status.BAD_REQUEST)
    val email = formData.get("email") ?: return Response(Status.BAD_REQUEST)

    transaction {
        Users.insert {
            it[Users.name] = name
            it[Users.email] = email
        }
    }

    return Response(Status.SEE_OTHER).header("Location", "/")
}

fun getUserDetails(request: Request): Response {
    val id = request.path("id")?.toIntOrNull() ?: return Response(Status.BAD_REQUEST)
    val user = transaction {
        Users.select { Users.id eq id }.map { User(it[Users.id], it[Users.name], it[Users.email]) }.singleOrNull()
    }

    return if (user != null) {
        Response(Status.OK).body(renderUserDetails(user))
    } else {
        Response(Status.NOT_FOUND).body("User not found")
    }
}

fun renderUserList(users: List<User>): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <title>User List</title>
            <script src="https://unpkg.com/htmx.org@1.8.0"></script>
        </head>
        <body>
            <h1>User List</h1>
            <ul>
                ${users.joinToString("") { "<li><a href='/users/${it.id}'>${it.name}</a></li>" }}
            </ul>
            <h2>Add User</h2>
            <form hx-post="/users" hx-target="body">
                <input type="text" name="name" placeholder="Name" required>
                <input type="email" name="email" placeholder="Email" required>
                <button type="submit">Add User</button>
            </form>
        </body>
        </html>
    """.trimIndent()
}

fun renderUserDetails(user: User): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <title>User Details</title>
        </head>
        <body>
            <h1>User Details</h1>
            <p>ID: ${user.id}</p>
            <p>Name: ${user.name}</p>
            <p>Email: ${user.email}</p>
            <a href="/">Back to User List</a>
        </body>
        </html>
    """.trimIndent()

