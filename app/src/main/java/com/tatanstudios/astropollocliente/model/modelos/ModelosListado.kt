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