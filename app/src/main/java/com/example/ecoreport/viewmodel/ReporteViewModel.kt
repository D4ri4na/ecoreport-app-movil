package com.example.ecoreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.data.remote.CrearReporteDto
import com.example.ecoreport.data.remote.ReporteDto
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ReporteViewModel (
    private val repository: ReporteRepository
): ViewModel() {

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)

    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada
    val reportes:
    StateFlow<List<ReporteEntity>> =
        _categoriaSeleccionada
            .flatMapLatest {
                categoria ->
                repository.obtenerPorCategoria(categoria)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted
                    .WhileSubscribed(5000),
                initialValue = emptyList()
            )



    fun insertar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.insertar(reporte)
        }
    }

    fun eliminar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.eliminar(reporte)
        }
    }

    fun actualizar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.actualizar(reporte)
        }
    }

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = categoria
    }

    // Datos remotos
    private val _reportesRemotos = MutableStateFlow<List<ReporteDto>>(emptyList())
    val reportesRemotos: StateFlow<List<ReporteDto>> = _reportesRemotos
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarReportesRemotos() {
        viewModelScope.launch {
            ejecutarOperacionRemota("No se pudieron cargar los reportes") {
                _reportesRemotos.value = repository.obtenerReportesRemotos()
            }
        }
    }

    fun crearReporteRemoto(
        titulo: String,
        descripcion: String,
        categoria: String,
        ubicacion: String,
        estado: String,
        fecha: String
    ) {
        viewModelScope.launch {
            ejecutarOperacionRemota("No se pudo crear el reporte") {
                val reporteCreado = repository.crearReporte(
                    CrearReporteDto(
                        titulo = titulo,
                        descripcion = descripcion,
                        categoria = categoria,
                        ubicacion = ubicacion,
                        estado = estado,
                        fecha = fecha
                    )
                )

                _reportesRemotos.value = listOf(reporteCreado) + _reportesRemotos.value
            }
        }
    }

    fun actualizarReporteRemoto(
        id: String,
        titulo: String,
        descripcion: String,
        categoria: String,
        ubicacion: String,
        estado: String,
        fecha: String
    ) {
        viewModelScope.launch {
            ejecutarOperacionRemota("No se pudo actualizar el reporte") {
                val reporteActualizado = repository.editarReporte(
                    id = id,
                    reporteActualizado = CrearReporteDto(
                        titulo = titulo,
                        descripcion = descripcion,
                        categoria = categoria,
                        ubicacion = ubicacion,
                        estado = estado,
                        fecha = fecha
                    )
                )

                _reportesRemotos.value = _reportesRemotos.value.map { reporte ->
                    if (reporte.id == reporteActualizado.id) reporteActualizado else reporte
                }
            }
        }
    }

    fun eliminarReporteRemoto(id: String) {
        viewModelScope.launch {
            ejecutarOperacionRemota("No se pudo eliminar el reporte") {
                repository.eliminarReporte(id)
                _reportesRemotos.value = _reportesRemotos.value.filterNot { reporte ->
                    reporte.id == id
                }
            }
        }
    }

    private suspend fun ejecutarOperacionRemota(
        mensajeError: String,
        operacion: suspend () -> Unit
    ) {
        _cargando.value = true
        _error.value = null

        try {
            operacion()
        } catch (e: Exception) {
            _error.value = mensajeError
        } finally {
            _cargando.value = false
        }
    }
}
