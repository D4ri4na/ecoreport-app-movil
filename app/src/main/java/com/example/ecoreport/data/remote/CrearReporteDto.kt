package com.example.ecoreport.data.remote

import com.google.gson.annotations.SerializedName

data class CrearReporteDto(
    @SerializedName("name")
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val ubicacion: String,
    val estado: String,
    val fecha: String
)
