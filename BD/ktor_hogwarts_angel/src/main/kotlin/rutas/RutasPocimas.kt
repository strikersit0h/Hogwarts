package com.example.rutas

import com.example.DAO.PocimaDAO
import com.example.DAO.PocimaDAOImpl
import com.example.models.Pocima
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pocimaRouting() {
    val pocimaDAO: PocimaDAO = PocimaDAOImpl()

    route("/pocimas") {
        get {
            val pocimas = pocimaDAO.obtenerTodas()
            call.respond(HttpStatusCode.OK, pocimas)
        }

        post {
            try {
                val pocima = call.receive<Pocima>()
                val pocimaCreada = pocimaDAO.crear(pocima)
                if (pocimaCreada != null) {
                    call.respond(HttpStatusCode.Created, pocimaCreada)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Error al crear la pócima.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos de pócima inválidos.")
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de pócima no válido.")
                return@put
            }
            try {
                val pocima = call.receive<Pocima>()
                if (pocimaDAO.actualizar(id, pocima)) {
                    call.respond(HttpStatusCode.OK, "Pócima actualizada.")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Pócima no encontrada.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos de pócima inválidos.")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de pócima no válido.")
                return@delete
            }
            if (pocimaDAO.eliminar(id)) {
                call.respond(HttpStatusCode.OK, "Pócima eliminada.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Pócima no encontrada.")
            }
        }
    }
}