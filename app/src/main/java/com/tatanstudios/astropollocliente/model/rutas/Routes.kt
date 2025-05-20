package com.tatanstudios.astropollocliente.model.rutas

sealed class Routes(val route: String) {
    object VistaSplash: Routes("splash")
    object VistaLogin: Routes("login")
    object VistaRegistro: Routes("registro")



    object VistaPrincipal: Routes("principal")





}