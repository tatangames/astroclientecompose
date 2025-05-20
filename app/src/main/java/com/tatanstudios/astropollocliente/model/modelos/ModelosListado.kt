package com.tatanstudios.astropollocliente.model.modelos

import com.google.gson.annotations.SerializedName

data class ModeloDatosBasicos(
    @SerializedName("success") val success: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("titulo") val titulo: String?,
    @SerializedName("mensaje") val mensaje: String?
)

