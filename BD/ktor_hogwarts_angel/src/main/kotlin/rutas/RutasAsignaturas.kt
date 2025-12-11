package com.example.rutas

import com.example.DAO.AsignaturaDAO
import com.example.DAO.AsignaturaDAOImpl
import com.example.models.Asignatura
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.asignaturaRouting() {
    val asignaturaDAO: AsignaturaDAO = AsignaturaDAOImpl()

    route("/asignaturas") {
        get {
            call.respond(asignaturaDAO.obtenerTodas())
        }

        post {
            try {
                val asignatura = call.receive<Asignatura>()
                val asignaturaCreada = asignaturaDAO.crear(asignatura)
                call.respond(HttpStatusCode.Created, asignaturaCreada!!)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos de asignatura inválidos.")
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest, "ID no válido")
            try {
                val asignatura = call.receive<Asignatura>()
                if (asignaturaDAO.actualizar(id, asignatura)) {
                    call.respond(HttpStatusCode.OK, "Asignatura actualizada.")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Asignatura no encontrada.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos de asignatura inválidos.")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest, "ID no válido")
            if (asignaturaDAO.eliminar(id)) {
                call.respond(HttpStatusCode.OK, "Asignatura eliminada.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Asignatura no encontrada.")
            }
        }

        route("/{id}/usuarios") {
            post {
                val idAsignatura = call.parameters["id"]?.toIntOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest, "ID de asignatura no válido.")
                try {
                    val body = call.receive<Map<String, Int>>()
                    val idUsuario = body["id_usuario"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Falta 'id_usuario'.")

                    if (asignaturaDAO.asignarUsuario(idAsignatura, idUsuario)) {
                        call.respond(HttpStatusCode.OK, "Usuario asignado.")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "No se pudo asignar.")
                    }
                } catch (e: Exception) { call.respond(HttpStatusCode.BadRequest) }
            }

            get {
                val idAsignatura = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest, "ID de asignatura no válido.")
                val usuarios = asignaturaDAO.obtenerUsuariosDeAsignatura(idAsignatura)
                call.respond(HttpStatusCode.OK, usuarios)
            }

            delete("/{idUsuario}") {
                val idAsignatura = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest, "ID de asignatura no válido.")
                val idUsuario = call.parameters["idUsuario"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest, "ID de usuario no válido.")

                if (asignaturaDAO.desasignarUsuario(idAsignatura, idUsuario)) {
                    call.respond(HttpStatusCode.OK, "Usuario desasignado.")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "No se pudo desasignar.")
                }
            }
        }
    }
}