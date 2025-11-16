package com.example.hogwarts_angel.api

import com.example.hogwarts_angel.model.Usuario
import com.example.hogwarts_angel.model.UsuarioLogin
import com.example.hogwarts_angel.model.UsuarioRol
import retrofit2.Response
import retrofit2.http.*

interface HogwartsAPI {

    // 1. Obtener Usuario por ID (Coincide con GET /usuario/{id})
    @GET("usuario/{id}")
    suspend fun getUsuario(@Path("id") id: Int): Response<Usuario?>

    // 2. Obtener Rol del Usuario por ID
    @GET("usuario/{id}/rol")
    suspend fun getRolUsuario(@Path("id") id: Int): Response<List<UsuarioRol>>

    // 3. Login de Usuario
    @POST("login")
    suspend fun login(@Body usuarioLogin: UsuarioLogin): Response<Usuario?>

    // 4. Insertar/Crear Usuario
    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<Unit>
}