package com.example.rutas

import com.example.DAO.HechizoDAO
import com.example.DAO.HechizoDAOImpl
import com.example.models.Hechizo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.hechizoRouting() {
    val hechizoDAO: HechizoDAO = HechizoDAOImpl()

    route("/hechizos") {

        get {
            val hechizos = hechizoDAO.obtenerTodos()
            call.respond(HttpStatusCode.OK, hechizos)
        }

        post {
            try {
                val hechizo = call.receive<Hechizo>()
                val hechizoCreado = hechizoDAO.crear(hechizo)
                if (hechizoCreado != null) {
                    call.respond(HttpStatusCode.Created, hechizoCreado)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Error al crear el hechizo.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos de hechizo inválidos.")
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de hechizo no válido.")
                return@put
            }
            try {
                val hechizo = call.receive<Hechizo>()
                if (hechizoDAO.actualizar(id, hechizo)) {
                    call.respond(HttpStatusCode.OK, "Hechizo actualizado.")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Hechizo no encontrado.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos de hechizo inválidos.")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de hechizo no válido.")
                return@delete
            }
            if (hechizoDAO.eliminar(id)) {
                call.respond(HttpStatusCode.OK, "Hechizo eliminado.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Hechizo no encontrado.")
            }
        }
    }
}