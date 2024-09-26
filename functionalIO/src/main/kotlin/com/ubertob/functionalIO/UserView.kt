package com.ubertob.functionalIO


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
}