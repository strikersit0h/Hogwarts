package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Ingrediente(
    val id: Int,
    val nombre: String,
    val sanacion: Int,
    val analgesia: Int,
    val curativo: Int,
    val inflamatorio: Int
)