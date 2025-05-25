package com.tatanstudios.astropollocliente.model.modelos

import com.google.gson.annotations.SerializedName

data class ModeloDatosBasicos(
    @SerializedName("success") val success: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("titulo") val titulo: String?,
    @SerializedName("mensaje") val mensaje: String?
)





data class ModeloMenuPrincipal(
    @SerializedName("success") val success: Int,
    @SerializedName("titulo") val titulo: String?,
    @SerializedName("mensaje") val mensaje: String?,
    @SerializedName("activo") val activo: Int,
    @SerializedName("slider") val arraySlider: List<ModeloMenuPrincipalSliderArray>,
    @SerializedName("categorias") val arrayCategorias: List<ModeloMenuPrincipalCategoriasArray>,
    @SerializedName("populares") val arrayPopulares: List<ModeloMenuPrincipalProductosArray>,
    @SerializedName("activo_slider") val activoSlider: Int,
    @SerializedName("btntesteocliente") val btnTesteoCliente: Int,
    @SerializedName("btntesteoservicio") val btnTesteoServicio: Int,
)

data class ModeloMenuPrincipalSliderArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_producto") val idProducto: Int,
    @SerializedName("id_servicios") val idServicios: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("redireccionamiento") val redireccionamiento: Int,
    @SerializedName("usa_horario") val usaHorario: Int,
    @SerializedName("hora_abre") val horaAbre: String?,
    @SerializedName("hora_cierra") val horaCierra: String?,
    @SerializedName("activo") val activo: Int,
)

data class ModeloMenuPrincipalCategoriasArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_servicios") val idServicios: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("activo") val activo: Int,
)

data class ModeloMenuPrincipalProductosArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_productos") val idProducto: Int,
    @SerializedName("id_servicios") val idServicios: Int,
    @SerializedName("id_categorias") val idCategoria: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio") val precio: String?,
    @SerializedName("activo") val activo: Int,
    @SerializedName("utiliza_nota") val utilizaNota: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("utiliza_imagen") val utilizaImagen: Int,
)






data class ModeloHistorialOrdenes(
    @SerializedName("success") val success: Int,
    @SerializedName("hayordenes") val hayordenes: Int,
    @SerializedName("ordenes") val lista: List<ModeloHistorialOrdenesArray>
)


data class ModeloHistorialOrdenesArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_cliente") val idcliente: Int,
    @SerializedName("id_servicio") val idservicio: Int,
    @SerializedName("id_zona") val idzona: Int,
    @SerializedName("nota_orden") val notaOrden: String?,
    @SerializedName("totalformat") val totalFormat: String?,
    @SerializedName("estado") val estado: String?,
    @SerializedName("fecha_orden") val fechaOrden: String?,
    @SerializedName("fecha_cancelada") val fechaCancelada: String?,
    @SerializedName("haycupon") val haycupon: Int,
    @SerializedName("cliente") val cliente: String?,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("referencia") val referencia: String?,
    @SerializedName("haypremio") val haypremio: Int,
    @SerializedName("textopremio") val textopremio: String?,
    @SerializedName("mensaje_cupon") val mensajeCupon: String?,
)





data class ModeloProductoHistorialOrdenes(
    @SerializedName("success") val success: Int,
    @SerializedName("productos") val lista: List<ModeloProductoHistorialOrdenesArray>
)

data class ModeloProductoHistorialOrdenesArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_ordenes") val idordenes: Int,
    @SerializedName("id_producto") val idproducto: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("precio") val precio: String?,
    @SerializedName("nombreproducto") val nombreProducto: String?,
    @SerializedName("idordendescrip") val idOrdenDescrip: Int,
    @SerializedName("utiliza_imagen") val utilizaImagen: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("multiplicado") val multiplicado: String?,
)




data class ModeloInfoProducto(
    @SerializedName("success") val success: Int,
    @SerializedName("productos") val lista: List<ModeloInfoProductoArray>
)

data class ModeloInfoProductoArray(
    @SerializedName("id") val success: Int,
    @SerializedName("id_ordenes") val idordenes: Int,
    @SerializedName("id_producto") val idproducto: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("precio") val precio: String?,
    @SerializedName("nombreproducto") val nombreProducto: String?,
    @SerializedName("utiliza_imagen") val utilizaImagen: Int,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("multiplicado") val multiplicado: String?,
)

data class ModeloHorario(
    @SerializedName("success") val success: Int,
    @SerializedName("restaurante") val restaurante: String,
    @SerializedName("horario") val lista: List<ModeloHorarioArray>
)


data class ModeloHorarioArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_servicios") val idServicio: Int,
    @SerializedName("hora1") val hora1: String?,
    @SerializedName("hora2") val hora2: String?,
    @SerializedName("dia") val dia: Int,
    @SerializedName("cerrado") val cerrado: Int,
    @SerializedName("horario") val horario: String?,
    @SerializedName("fechaformat") val fechaformat: String?,
)




data class ModeloPremios(
    @SerializedName("success") val success: Int,
    @SerializedName("conteo") val conteo: Int,
    @SerializedName("nota") val nota: String?,
    @SerializedName("puntos") val puntos: String?,
    @SerializedName("listado") val lista: List<ModeloPremiosArray>
)


data class ModeloPremiosArray(
    @SerializedName("id") val id: Int,
    @SerializedName("id_servicio") val idServicio: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("puntos") val puntos: Int,
    @SerializedName("activo") val activo: Int,
    @SerializedName("seleccionado") var seleccionado: Int,
)




data class ModeloDirecciones(
    @SerializedName("success") val success: Int,
    @SerializedName("titulo") val titulo: String?,
    @SerializedName("mensaje") val mensaje: String?,
    @SerializedName("direcciones") val lista: List<ModeloDireccionesArray>
)

data class ModeloDireccionesArray(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("numero_casa") val numero_casa: String?,
    @SerializedName("punto_referencia") val punto_referencia: String?,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("seleccionado") var seleccionado: Int,
    @SerializedName("minimocompra") val minimocompra: String?,
)



data class ModeloPoligonos(
    @SerializedName("success") val success: Int,
    @SerializedName("poligono") val lista: List<ModeloPoligonosArray>
)

data class ModeloPoligonosArray(
    @SerializedName("id") val id: Int,
    @SerializedName("nombreZona") val nombre: String?,
    @SerializedName("poligonos") val listado: List<ModeloPoligonosLatitudLongitudArray>
)

data class ModeloPoligonosLatitudLongitudArray(
    @SerializedName("latitudPoligono") val latitud: String,
    @SerializedName("longitudPoligono") val longitud: String,
)