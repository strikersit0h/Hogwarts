package com.example.hogwarts_angel.model

data class Pocima(
    val id: Int? = null,
    val nombre: String,
    val fecha_creacion: String? = null,
    val fecha_modificacion: String? = null,
    val fecha_borrado: String? = null,
    val indicador_bien_mal: Boolean = false,
    val id_creador: Int,
    val validada: Boolean = false
)
