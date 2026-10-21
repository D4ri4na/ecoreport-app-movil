package com.example.ecoreport

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ecoreport.data.AppDatabase
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.repository.ReporteRepository
import com.example.ecoreport.ui_comp.screens.PantallaReportes
import com.example.ecoreport.viewmodel.ReporteViewModel
import com.example.ecoreport.viewmodel.ReporteViewModelFactory
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaReportesFiltrosTest {
    @get:Rule val compose = createComposeRule()
    private lateinit var database: AppDatabase

    @After
    fun cerrar() { if (::database.isInitialized) database.close() }

    @Test
    fun buscarAplicaLosControlesYPermiteRestablecerlos() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java
        ).build()
        runBlocking {
            listOf("Agua" to "PENDIENTE", "Residuos" to "RESUELTO").forEach { (categoria, estado) ->
                database.reporteDao().insertar(ReporteEntity(
                    titulo = "Caso $categoria", descripcion = "Prueba", categoria = categoria,
                    ubicacion = "La Paz", estado = estado, fecha = "21/10/2026"
                ))
            }
        }
        val factory = ReporteViewModelFactory(ReporteRepository(database.reporteDao()))
        compose.setContent {
            val vm: ReporteViewModel = viewModel(factory = factory)
            val reportes by vm.reportesFiltrados.collectAsState()
            val estado by vm.estadoSeleccionado.collectAsState()
            val categoria by vm.categoriaSeleccionada.collectAsState()
            MaterialTheme {
                PantallaReportes(
                    reportes = reportes, estadoSeleccionado = estado,
                    categoriaSeleccionada = categoria, seleccionarEstado = vm::seleccionarEstado,
                    seleccionarCategoria = vm::seleccionarCategoria, buscar = vm::buscarReportes,
                    seleccionarReporte = {}, volver = {}
                )
            }
        }
        compose.waitUntil(10_000) {
            compose.onAllNodesWithText("Caso Agua").fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("Estado").performClick()
        compose.onNodeWithText("Pendiente").performClick()
        compose.onNodeWithText("Categoría", substring = false).performTextInput("  agua  ")
        compose.onNodeWithText("Caso Residuos").assertExists()
        compose.onNodeWithText("Buscar").performClick()
        compose.waitUntil(10_000) {
            compose.onAllNodesWithText("Caso Residuos").fetchSemanticsNodes().isEmpty()
        }
        compose.onNodeWithText("Caso Agua").assertExists()
        compose.onNodeWithText("Categoría", substring = false).performTextClearance()
        compose.onNodeWithText("Estado").performClick()
        compose.onNodeWithText("Todos").performClick()
        compose.onNodeWithText("Buscar").performClick()
        compose.waitUntil(10_000) {
            compose.onAllNodesWithText("Caso Residuos").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
