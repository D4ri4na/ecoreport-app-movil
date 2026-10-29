package com.example.ecoreport

import com.example.ecoreport.data.remote.ReporteDto
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReporteDtoTest {
    @Test
    fun interpretaElFormatoRealDeLaApiSinFecha() {
        val json = """[{"id":"1","titulo":"Reporte API","descripcion":"Descripción","categoria":"Agua","ubicacion":"La Paz","estado":"PENDIENTE"}]"""
        val reportes = Gson().fromJson(json, Array<ReporteDto>::class.java)
        assertEquals("1", reportes.single().id)
        assertEquals("Reporte API", reportes.single().titulo)
        assertNull(reportes.single().fecha)
    }

    @Test
    fun conservaLaFechaCuandoLaApiLaIncluye() {
        val json = """{"id":"2","titulo":"Reporte","descripcion":"Descripción","categoria":"Agua","ubicacion":"La Paz","estado":"RESUELTO","fecha":"28/10/2026"}"""
        assertEquals("28/10/2026", Gson().fromJson(json, ReporteDto::class.java).fecha)
    }
    @Test
    fun admiteLosRegistrosGeneradosSinCamposDeReporte() {
        val json = """{"id":"3","name":"Ejemplo generado","avatar":"https://example.com/avatar.png"}"""
        val reporte = Gson().fromJson(json, ReporteDto::class.java)
        assertEquals("Ejemplo generado", reporte.nombre)
        assertNull(reporte.titulo)
        assertNull(reporte.descripcion)
    }}
