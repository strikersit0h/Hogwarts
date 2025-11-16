package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioLogin(val email: String, val password:String)
