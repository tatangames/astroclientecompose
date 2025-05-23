package com.tatanstudios.astropollocliente.network

import com.tatanstudios.astropollocliente.model.modelos.ModeloDatosBasicos
import com.tatanstudios.astropollocliente.model.modelos.ModeloHistorialOrdenes
import com.tatanstudios.astropollocliente.model.modelos.ModeloHorario
import com.tatanstudios.astropollocliente.model.modelos.ModeloInfoProducto
import com.tatanstudios.astropollocliente.model.modelos.ModeloMenuPrincipal
import com.tatanstudios.astropollocliente.model.modelos.ModeloPremios
import com.tatanstudios.astropollocliente.model.modelos.ModeloProductoHistorialOrdenes
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



    // ENVIAR CORREO PARA QUE LARAVEL ENVIE CORREO CODIGO
    @POST("cliente/enviar/codigo-correo")
    @FormUrlEncoded
    fun enviarCorreoApi(@Field("correo") correo: String,
    ): Single<ModeloDatosBasicos>



    // ACTUALIZAR PASSWORD EMAIL
    @POST("cliente/actualizar/password")
    @FormUrlEncoded
    fun resetearPasswordEmailApi(@Field("id") id: String,
                                 @Field("password") password: String,
    ): Single<ModeloDatosBasicos>


    // VERIFICAR CODIGO QUE SE ENVIO AL CORREO
    @POST("cliente/verificar/codigo-correo-password")
    @FormUrlEncoded
    fun verificarCodigoEmailApi(@Field("codigo") codigo: String,
                                 @Field("correo") correo: String,
    ): Single<ModeloDatosBasicos>




    //*****************************************************************************
    @POST("cliente/lista/servicios-bloque")
    @FormUrlEncoded
    fun listadoMenuPrincipal(@Field("id") id: String,
    ): Single<ModeloMenuPrincipal>


    // LISTADO DE ORDENES DE HISTORIAL SEGUN FECHA
    @POST("cliente/historial/listado/ordenes")
    @FormUrlEncoded
    fun listadoHistorialOrdenes(@Field("id") id: String,
                                @Field("fecha1") fecha1: String,
                                @Field("fecha2") fecha2: String,
    ): Single<ModeloHistorialOrdenes>


    // LISTADO DE PRODUCTOS DE UNA ORDEN
    @POST("cliente/listado/productos/ordenes")
    @FormUrlEncoded
    fun listadoProductosHistorialOrden(@Field("ordenid") ordenid: Int,
    ): Single<ModeloProductoHistorialOrdenes>


    // INFO DE UN PRODUCTO DE UNA ORDEN
    @POST("cliente/listado/productos/ordenes-individual")
    @FormUrlEncoded
    fun infoProductosHistorialOrden(@Field("idordendescrip") idordendescrip: Int,
    ): Single<ModeloInfoProducto>


    // ACTUALIZAR CONTRASENA
    @POST("cliente/perfil/actualizar/contrasena")
    @FormUrlEncoded
    fun actualizarPassword(@Field("id") id: String,
                           @Field("password") password: String,
    ): Single<ModeloDatosBasicos>



    // LISTADO DE HORARIOS
    @POST("cliente/informacion/restaurante/horario")
    @FormUrlEncoded
    fun listadoHorario(@Field("id") id: String,
    ): Single<ModeloHorario>


    // LISTADO DE PREMIOS
    @POST("cliente/premios/listado")
    @FormUrlEncoded
    fun listadoPremios(@Field("clienteid") clienteid: String,
    ): Single<ModeloPremios>





}


