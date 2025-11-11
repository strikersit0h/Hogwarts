package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioLogin(val id: Int, val password:String)
