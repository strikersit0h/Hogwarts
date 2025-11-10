package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioLogin(val nombre: String, val password:String)
