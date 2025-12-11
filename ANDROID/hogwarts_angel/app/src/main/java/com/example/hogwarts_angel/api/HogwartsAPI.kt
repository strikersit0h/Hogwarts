package com.example.hogwarts_angel.api

import com.example.hogwarts_angel.model.Asignatura
import com.example.hogwarts_angel.model.Hechizo
import com.example.hogwarts_angel.model.Ingrediente
import com.example.hogwarts_angel.model.Pocima
import com.example.hogwarts_angel.model.Usuario
import com.example.hogwarts_angel.model.UsuarioDetallado
import com.example.hogwarts_angel.model.UsuarioLogin
import com.example.hogwarts_angel.model.UsuarioModificar
import com.example.hogwarts_angel.model.UsuarioRegistro
import com.example.hogwarts_angel.model.UsuarioRol
import retrofit2.Response
import retrofit2.http.*

interface HogwartsAPI {

    // --- USUARIOS ---
    @GET("usuarios")
    suspend fun getTodosLosUsuarios(): Response<List<Usuario>>

    @GET("usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): Response<Usuario?>

    @GET("usuarios/{id}/rol")
    suspend fun getRolUsuario(@Path("id") id: Int?): Response<List<UsuarioRol>>

    @POST("usuarios/login")
    suspend fun login(@Body usuarioLogin: UsuarioLogin): Response<Usuario?>

    @POST("usuarios/registro")
    suspend fun crearUsuario(@Body usuarioRegistro: UsuarioRegistro): Response<Unit>

    @POST("usuarios/registroDirecto")
    suspend fun crearUsuarioDirecto(@Body usuario: Usuario): Response<Unit>

    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int?): Response<Unit>

    @PUT("usuarios")
    suspend fun modificarUsuario(@Body usuarioModificar: UsuarioModificar): Response<Unit>

    @GET("usuarios/detallados")
    suspend fun getTodosLosUsuariosDetallados(): Response<List<UsuarioDetallado>>

    @PUT("usuarios/{id}/roles")
    suspend fun actualizarRolesUsuario(@Path("id") id: Int, @Body roles: List<Int>): Response<Unit>

    // --- ASIGNATURAS ---
    @GET("asignaturas")
    suspend fun getAsignaturas(): Response<List<Asignatura>>

    @POST("asignaturas")
    suspend fun crearAsignatura(@Body asignatura: Asignatura): Response<Unit>

    @PUT("asignaturas/{id}")
    suspend fun actualizarAsignatura(@Path("id") id: Int, @Body asignatura: Asignatura): Response<Unit>

    @DELETE("asignaturas/{id}")
    suspend fun eliminarAsignatura(@Path("id") id: Int): Response<Unit>

    // --- HECHIZOS ---
    @GET("hechizos")
    suspend fun getHechizos(): Response<List<Hechizo>>

    @POST("hechizos")
    suspend fun crearHechizo(@Body hechizo: Hechizo): Response<Unit>

    @PUT("hechizos/{id}")
    suspend fun actualizarHechizo(@Path("id") id: Int?, @Body hechizo: Hechizo): Response<Unit>

    @DELETE("hechizos/{id}")
    suspend fun eliminarHechizo(@Path("id") id: Int?): Response<Unit>

    @PUT("hechizos/{id}/validar")
    suspend fun validarHechizo(@Path("id") id: Int, @Body validada: Map<String, Boolean>): Response<Unit>

    // --- PÓCIMAS ---
    @GET("pocimas")
    suspend fun getPocimas(): Response<List<Pocima>>

    @POST("pocimas")
    suspend fun crearPocima(@Body pocima: Pocima): Response<Unit>

    @PUT("pocimas/{id}")
    suspend fun actualizarPocima(@Path("id") id: Int, @Body pocima: Pocima): Response<Unit>

    @DELETE("pocimas/{id}")
    suspend fun eliminarPocima(@Path("id") id: Int): Response<Unit>

    @PUT("pocimas/{id}/validar")
    suspend fun validarPocima(@Path("id") id: Int, @Body validada: Map<String, Boolean>): Response<Unit>
    
    // --- INGREDIENTES ---
    @GET("ingredientes")
    suspend fun getIngredientes(): Response<List<Ingrediente>>
}
