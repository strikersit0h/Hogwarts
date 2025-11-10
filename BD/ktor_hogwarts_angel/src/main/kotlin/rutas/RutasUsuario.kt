package com.example.rutas

import io.ktor.server.routing.Route

fun Route.userRouting(){
    val usuarioDAO: UsuarioDAO = UsuarioDAOImpl()
}