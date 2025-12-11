package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDetallado(
    val id: Int?,
    val nombre: String,
    val nivel: Int,
    val experiencia: Int,
    val id_roles: List<Int>,
    val id_casa: Int
)