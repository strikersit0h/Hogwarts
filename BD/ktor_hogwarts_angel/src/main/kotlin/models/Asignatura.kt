package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Asignatura(
    val id: Int,
    val nombre: String,
    val descripcion: String
)