package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioModificar (val id: Int, var nombre: String, var nivel: Int, var experiencia: Int)