package com.example.models

import kotlinx.serialization.Serializable

// Este es el objeto que el cliente Android enviará a Ktor
@Serializable
data class UsuarioRegistro(val nombre: String, val email: String, val contrasenya: String, val respuestasSombrero: SombreroRespuestas)