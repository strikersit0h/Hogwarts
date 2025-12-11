package com.example.models

import kotlinx.serialization.Serializable


@Serializable
data class Hechizo(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val puntos_experiencia: Int
)
