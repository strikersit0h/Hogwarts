package com.example.rutas

import com.example.DAO.UsuarioDAO
import com.example.DAO.UsuarioDAOImpl
import com.example.models.SombreroRespuestas
import com.example.models.Usuario
import com.example.models.UsuarioLogin
import com.example.models.UsuarioModificar
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

        get("/detallados") {
            try {
                val usuarios = usuarioDAO.obtenerTodosLosUsuariosDetallados()
                call.respond(HttpStatusCode.OK, usuarios)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al obtener usuarios detallados: ${e.message}")
            }
        }

        put("/{id}/roles") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de usuario no válido.")
                    return@put
                }

                // Recibe una lista de IDs de rol: [2, 3]
                val nuevosRoles = call.receive<List<Int>>()
                val actualizado = usuarioDAO.actualizarRolesUsuario(id, nuevosRoles)

                if (actualizado) {
                    call.respond(HttpStatusCode.OK, "Roles actualizados para el usuario $id.")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "No se pudieron actualizar los roles.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor: ${e.message}")
            }
        }

        get {
            val usuarios = usuarioDAO.obtenerTodos()

            if (usuarios.isNotEmpty()){
                call.respond(HttpStatusCode.OK, usuarios)
            }else{
                call.respond(HttpStatusCode.OK, "La request ha sido correcta pero no hay usuarios.")
            }
        }

        put {
            try {
                val usuarioModificar = call.receive<UsuarioModificar>()
                val actualizado = usuarioDAO.actualizarUsuario(usuarioModificar)

                if (actualizado) {
                    call.respond(HttpStatusCode.OK, "Usuario con ID ${usuarioModificar.id} actualizado correctamente.")
                } else {
                    call.respond(HttpStatusCode.NotFound, "No se pudo encontrar o actualizar el usuario con ID ${usuarioModificar.id}.")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Formato de datos de modificación inválido. Asegúrate de enviar {id, nombre, nivel, experiencia}.")
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error interno al intentar actualizar el usuario: ${e.message}")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de usuario inválido.")
                return@delete
            }

            try {
                val eliminado = usuarioDAO.eliminarUsuario(id)
                if (eliminado) {
                    call.respond(HttpStatusCode.NoContent, "Usuario con ID $id eliminado correctamente.")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario con ID $id no encontrado.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error interno al intentar eliminar el usuario.")
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

        post("/registro") {
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
                    id_casa = casaAsignadaId,
                    id = 0
                )

                val idAutogenerada = usuarioDAO.insertarUsuario(nuevoUsuario)

                if (idAutogenerada != null && idAutogenerada > 0) {
                    val rolAsignadoExitoso = usuarioDAO.insertarUsuarioRol(idAutogenerada)

                    if (rolAsignadoExitoso) {
                        call.respond(HttpStatusCode.Created, "Casa asignada: $casaAsignadaId y rol de Usuario asignado")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Error: Usuario creado, pero fallo al asignar rol.")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Error al registrar el usuario principal (datos duplicados o fallo de DB).")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Formato de datos de registro inválido.")
            } catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor: ${e.message}")
            }
        }

        post("/registroDirecto"){
            try {
                val nuevoUsuario = call.receive<Usuario>()

                val idAutogenerada = usuarioDAO.insertarUsuario(nuevoUsuario)

                if (idAutogenerada != null && idAutogenerada > 0) {
                    val rolAsignadoExitoso = usuarioDAO.insertarUsuarioRol(idAutogenerada)

                    if (rolAsignadoExitoso) {
                        call.respond(HttpStatusCode.Created, "Usuario ${nuevoUsuario.nombre} registrado con ID $idAutogenerada y rol por defecto (Usuario).")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Error: Usuario creado, pero fallo al asignar rol.")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Error al registrar el usuario principal (datos duplicados o fallo de DB).")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Formato de datos de registro inválido.")
            } catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor: ${e.message}")
            }
        }
    }

}