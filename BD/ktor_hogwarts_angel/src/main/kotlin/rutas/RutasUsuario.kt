package com.example.rutas

import com.example.DAO.UsuarioDAO
import com.example.DAO.UsuarioDAOImpl
import com.example.models.Usuario
import com.example.models.UsuarioLogin
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting(){
    val usuarioDAO: UsuarioDAO = UsuarioDAOImpl()

    route("/usuarios") {
        get {
            call.respond(HttpStatusCode.NotImplemented, "Obtener todos los usuarios aún no implementado.")
        }

        post {
            try {
                val usuario = call.receive<Usuario>()
                if (usuarioDAO.insertar(usuario)) {
                    call.respondText("Usuario registrado con éxito", status = HttpStatusCode.Created)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Error al registrar el usuario. Puede que el email ya exista o haya un error en la BBDD.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor.")
            }
        }
    }

    route("/usuario/{id}") {
        get {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de usuario inválido.")
                return@get
            }

            val usuario = usuarioDAO.obtener(id)
            if (usuario != null) {
                call.respond(usuario)
            } else {
                call.respond(HttpStatusCode.NotFound, "Usuario con ID $id no encontrado.")
            }
        }

        get("/rol") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de usuario inválido.")
                return@get
            }

            val usuarioRoles = usuarioDAO.obtenerRolUsuario(id)

            call.respond(usuarioRoles)
        }
    }

    post("/login") {
        try {
            val usuarioLogin = call.receive<UsuarioLogin>()
            val usuario = usuarioDAO.login(usuarioLogin)

            if (usuario != null) {
                call.respond(usuario)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Email o Contraseña incorrectos.")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor.")
        }
    }
}