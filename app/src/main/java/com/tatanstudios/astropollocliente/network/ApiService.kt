package com.tatanstudios.astropollocliente.network

import com.tatanstudios.astropollocliente.model.modelos.ModeloDatosBasicos
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    // VERIFICACION DE NUMERO
    @POST("cliente/login")
    @FormUrlEncoded
    fun verificarUsuarioPassword(@Field("usuario") telefono: String,
                          @Field("password") password: String,
                          @Field("idfirebase") idfirebase: String?
                          ): Single<ModeloDatosBasicos>



    // REGISTRARSE
    @POST("cliente/registro")
    @FormUrlEncoded
    fun registrarme(@Field("usuario") telefono: String,
                    @Field("password") password: String,
                    @Field("correo") correo: String?,
                    @Field("token_fcm") tokenFcm: String?,
                    @Field("version") version: String?
    ): Single<ModeloDatosBasicos>




}


