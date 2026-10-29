package com.example.ecoreport.data.remote

import com.google.gson.annotations.SerializedName

data class ReporteDto(
    val id: String,
    val titulo: String? = null,
    val descripcion: String? = null,
    val categoria: String? = null,
    val ubicacion: String? = null,
    val estado: String? = null,
    val fecha: String? = null,
    @SerializedName("name") val nombre: String? = null
)

//BASE URL = https://api.ejemplo.com/
//https://api.ejemplo.com/reportes ENDPOINT
//SELECCIONAR = BASE URL + reportes

//[ JSON
//    {
//        "id": 1,
//        "titulo": "Luminaria dañada",
//        "descripcion": "Poste sin iluminación",
//        "categoria": "LUMINARIA",
//        "ubicacion": "Av. Alemana",
//        "estado": "PENDIENTE"
//    },
//    {
//        "id": 2,
//        "titulo": "Fuga de agua",
//        "descripcion": "Pérdida de agua potable",
//        "categoria": "AGUAS",
//        "ubicacion": "Av. Busch",
//        "estado": "EN_PROCESO"
//    }
//]