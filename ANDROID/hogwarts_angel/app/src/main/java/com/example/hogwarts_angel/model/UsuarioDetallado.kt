package com.example.hogwarts_angel.model

data class UsuarioDetallado(
    val id: Int?,
    val nombre: String,
    val nivel: Int,
    val experiencia: Int,
    val id_roles: List<Int>,
    val id_casa: Int
)