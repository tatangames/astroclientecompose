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

    // HISTORIAL DONDE SE COLOCA LA FECHA
    object VistaHistorialFecha: Routes("historialFecha")
    // LISTA DE ORDENES DEL HISTORIAL
    object VistaHistorialListadoOrden: Routes("historialListadoOrdenes/{fecha1}/{fecha2}") {
        fun createRoute(fecha1: String, fecha2: String) = "historialListadoOrdenes/$fecha1/$fecha2"
    }
    // LISTADO DE PRODUCTOS HISTORIAL DE UNA ORDEN
    object VistaListadoProductosHistorialOrden: Routes("listadoProductosHistorialOrden/{idorden}") {
        fun createRoute(idorden: String) = "listadoProductosHistorialOrden/$idorden"
    }
    // VISTA INFO PRODUCTO PARA HISTORIAL ORDENES
    object VistaInfoProductoHistorialOrden: Routes("infoProductoHistorialOrden/{idproducto}") {
        fun createRoute(idproducto: String) = "infoProductoHistorialOrden/$idproducto"
    }

    // VISTA ACTUALIZAR CONTRASENA
    object VistaActualizarContrasena: Routes("actualizarContrasena")
    // VISTA HORARIOS
    object VistaHorarios: Routes("vistaHorarios")
    // VISTA PREMIOS
    object VistaPremios: Routes("vistaPremios")
    // VISTA MIS DIRECCIONES
    object VistaMisDirecciones : Routes("vistaMisDirecciones/{estadoBoton}") {
        fun createRoute(
            estadoBoton: Int
        ) = "vistaMisDirecciones/$estadoBoton"
    }


    // VISTA MAPA
    object VistaMapa: Routes("vistaMapa")

    // VISTA REGISTRO DE DIRECCION NUEVA
    object VistaRegistroDireccion: Routes("registroDireccionNueva/{id}/{latitud}/{longitud}/{latitudreal}/{longitudreal}") {
        fun createRoute(
            id: Int,
            latitud: Double,
            longitud: Double,
            latitudreal: Double?,
            longitudreal: Double?
        ) = "registroDireccionNueva/$id/$latitud/$longitud/${latitudreal ?: "none"}/${longitudreal ?: "none"}"
    }


    // VISTA SELECCIONAR DIRECCION
    object VistaSeleccionarDireccion: Routes("seleccionarDireccion/{id}/{nombre}/{telefono}/{direccion}/{referencia}") {
        fun createRoute(
            id: Int,
            nombre: String,
            telefono: String?,
            direccion: String?,
            referencia: String?
        ) = "seleccionarDireccion/$id/$nombre/$telefono/${direccion}/${referencia}"
    }


    // VISTA MI USUARIO
    object VistaMiUsuario: Routes("vistaMiUsuario")

    // VISTA LISTADO PRODUCTOS
    object VistaListadoProductos : Routes("vistaListadoProductos/{idcategoria}") {
        fun createRoute(
            idcategoria: Int
        ) = "vistaListadoProductos/$idcategoria"
    }


    // VISTA INFORMACION DE UN PRODUCTO
    object VistaInformacionProducto : Routes("vistaInformacionProducto/{idproducto}") {
        fun createRoute(
            idproducto: Int
        ) = "vistaInformacionProducto/$idproducto"
    }


    // VISTA PARA EDITAR PRODUCTO
    object VistaEditarProducto : Routes("vistaInformacionProductoEditar/{idfilaproducto}") {
        fun createRoute(
            idfilaproducto: Int
        ) = "vistaInformacionProductoEditar/$idfilaproducto"
    }



}