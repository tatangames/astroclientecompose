package com.tatanstudios.astropollocliente.model.rutas

sealed class Routes(val route: String) {
    object VistaSplash: Routes("splash")
    object VistaLogin: Routes("login")
    object VistaRegistro: Routes("registro")
    object VistaRecuperarCorreo: Routes("ingresoCorreo")
    object VistaCambiarPasswordCorreo: Routes("cambiarPasswordEmail/{idusuario}") {
        fun createRoute(idusuario: String) = "cambiarPasswordEmail/$idusuario"
    }




    object VistaPrincipal: Routes("principal")


    object VistaCarrito: Routes("carrito")
    object VistaPerfil: Routes("perfil")





}