package com.example.ecoreport

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class ReporteDaoTest {
    private lateinit var database: AppDatabase

    @Before
    fun crearBaseDeDatos() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java
        ).build()
    }

    @After
    fun cerrarBaseDeDatos() = database.close()

    @Test
    fun filtraPorEstadoYPermiteMostrarTodos() = runBlocking {
        val dao = database.reporteDao()
        listOf("PENDIENTE", "EN_PROCESO", "RESUELTO", "PENDIENTE").forEach { estado ->
            dao.insertar(ReporteEntity(
                titulo = "Reporte $estado", descripcion = "Descripción",
                categoria = "Residuos", ubicacion = "La Paz",
                estado = estado, fecha = "16/10/2026"
            ))
        }
        assertEquals(listOf(4, 3, 2, 1), dao.obtenerPorEstado(null).first().map { it.id })
        assertEquals(listOf(4, 1), dao.obtenerPorEstado("PENDIENTE").first().map { it.id })
        assertEquals(listOf(2), dao.obtenerPorEstado("EN_PROCESO").first().map { it.id })
        assertEquals(listOf(3), dao.obtenerPorEstado("RESUELTO").first().map { it.id })
        assertEquals(emptyList<ReporteEntity>(), dao.obtenerPorEstado("INEXISTENTE").first())

        val reporte = dao.obtenerPorEstado("PENDIENTE").first().first()
        dao.actualizar(reporte.copy(estado = "RESUELTO"))
        assertEquals(listOf(1), dao.obtenerPorEstado("PENDIENTE").first().map { it.id })
        assertEquals(listOf(4, 3), dao.obtenerPorEstado("RESUELTO").first().map { it.id })
        dao.eliminar(reporte)
        assertEquals(3, dao.obtenerTodos().first().size)
    }
}
