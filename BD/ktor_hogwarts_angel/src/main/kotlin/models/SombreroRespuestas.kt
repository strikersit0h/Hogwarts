package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class SombreroRespuestas(val respuestas: Map<Int, String>)