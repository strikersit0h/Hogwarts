package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(val id:Int? = null, val nombre: String, val email: String, val contrasenya: String, val experiencia: Int, val nivel: Int, val id_casa: Int)
