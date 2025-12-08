package com.example.rutas

import com.example.DAO.UsuarioDAO
import com.example.DAO.UsuarioDAOImpl
import com.example.models.SombreroRespuestas
import com.example.models.Usuario
import com.example.models.UsuarioLogin
import com.example.models.UsuarioRegistro
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun logicaSombreroSeleccionador(respuestas: SombreroRespuestas, poblacionesActuales: Map<Int, Int>): Int {
    val diferenciaMaximaPoblacion = 50

    val afinidad = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0)

    val reglas = mapOf(
        1 to mapOf("A" to 1, "B" to 2, "C" to 3, "D" to 4),
        2 to mapOf("A" to 1, "B" to 2, "C" to 3, "D" to 4)
    )

    respuestas.respuestas.forEach { (idPregunta, opcionElegida) ->
        val idCasaAfectada = reglas[idPregunta]?.get(opcionElegida)
        if (idCasaAfectada != null) {
            afinidad[idCasaAfectada] = afinidad.getValue(idCasaAfectada) + 5
        }
    }

    val poblacionMinima = poblacionesActuales.values.minOrNull() ?: 0
    val limiteMaximo = poblacionMinima + diferenciaMaximaPoblacion

    val ranking = afinidad.entries.sortedByDescending { it.value }

    for (entrada in ranking) {
        val idCasa = entrada.key
        val poblacionActual = poblacionesActuales.getOrDefault(idCasa, 0)

        if (poblacionActual <= limiteMaximo) {
            return idCasa
        }
    }

    return ranking.firstOrNull()?.key ?: 4
}

fun Route.userRouting(){
    val usuarioDAO: UsuarioDAO = UsuarioDAOImpl()

    route("/usuarios") {

        get {
            val usuarios = usuarioDAO.obtenerTodos()

            if (usuarios.isNotEmpty()){
                call.respond(HttpStatusCode.OK, usuarios)
            }else{
                call.respond(HttpStatusCode.OK, "La request ha sido correcta pero no hay usuarios.")
            }
        }

        route("/{id}") {

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

            route("/rol") {
                get {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "ID de usuario inválido.")
                        return@get
                    }

                    val usuarioRoles = usuarioDAO.obtenerRolUsuario(id)
                    call.respond(usuarioRoles)
                }
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

        post("/registro"){
            try {
                val datosRegistro = call.receive<UsuarioRegistro>()
                val poblacionesActuales = usuarioDAO.obtenerPoblacionesActuales()
                val casaAsignadaId = logicaSombreroSeleccionador(datosRegistro.respuestasSombrero, poblacionesActuales)

                val nuevoUsuario = Usuario(
                    nombre = datosRegistro.nombre,
                    email = datosRegistro.email,
                    contrasenya = datosRegistro.contrasenya,
                    experiencia = 0,
                    nivel = 1,
                    id_casa = casaAsignadaId
                )

                if (usuarioDAO.insertarUsuario(nuevoUsuario)) {
                    call.respondText("Usuario registrado con éxito. Casa asignada: $casaAsignadaId", status = HttpStatusCode.Created)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Error al registrar el usuario.")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Formato de datos de registro inválido.")
            } catch (e: Exception){
                call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor.")
            }
        }
    }

}