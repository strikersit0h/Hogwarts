package com.example.rutas

import com.example.DAO.IngredienteDAO
import com.example.DAO.IngredienteDAOImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ingredienteRouting() {
    val ingredienteDAO: IngredienteDAO = IngredienteDAOImpl()

    route("/ingredientes") {
        get {
            val ingredientes = ingredienteDAO.obtenerTodos()
            call.respond(HttpStatusCode.OK, ingredientes)
        }
    }
}