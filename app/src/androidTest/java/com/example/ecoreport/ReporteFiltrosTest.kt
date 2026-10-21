package com.example.ecoreport

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ecoreport.data.AppDatabase
import com.example.ecoreport.data.ReporteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReporteFiltrosTest {
    private lateinit var database: AppDatabase

    @Before
    fun preparar() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java
        ).build()
    }

    @After
    fun cerrar() = database.close()

    @Test
    fun combinaEstadoYCategoriaYReflejaCambios() = runBlocking {
        val dao = database.reporteDao()
        listOf(
            "PENDIENTE" to "Residuos",
            "RESUELTO" to "Residuos",
            "PENDIENTE" to "Agua"
        ).forEach { (estado, categoria) ->
            dao.insertar(ReporteEntity(
                titulo = categoria, descripcion = "Reporte de prueba", categoria = categoria,
                ubicacion = "La Paz", estado = estado, fecha = "21/10/2026"
            ))
        }
        assertEquals(listOf(3, 2, 1), dao.obtenerFiltrados(null, null).first().map { it.id })
        assertEquals(listOf(3, 1), dao.obtenerFiltrados("PENDIENTE", null).first().map { it.id })
        assertEquals(listOf(2, 1), dao.obtenerFiltrados(null, "residuos").first().map { it.id })
        val consulta = dao.obtenerFiltrados("PENDIENTE", "Residuos")
        assertEquals(listOf(1), consulta.first().map { it.id })
        assertEquals(emptyList<ReporteEntity>(), dao.obtenerFiltrados("RESUELTO", "Agua").first())
        val reporte = consulta.first().single()
        dao.actualizar(reporte.copy(estado = "RESUELTO"))
        assertEquals(emptyList<ReporteEntity>(), consulta.first())
        dao.eliminar(reporte)
        assertEquals(2, dao.obtenerFiltrados(null, null).first().size)
    }
}
